var drawingArea;
var lastId = 0;
var nodes = {};
var schemaVersion;

<!-- Look and feel of jsPlumb-->
var setJsPlumbDefaults = function() {
    jsPlumb.importDefaults({
        PaintStyle: {lineWidth:3, strokeStyle: '#0088cc'},
        Connector: 'Flowchart',
        ConnectionOverlays:[[ "Arrow", {location:-14, id:"arrow" } ]],
        Endpoints: [ "Dot", [ "Dot", { radius:6}]],
        EndpointStyle: {fillStyle:"#0088cc"},
        Anchors:["BottomCenter", "Continuous"],
        ReattachConnections:true
    });
};

var addNodeToModel = function(nodeModel) {
    if(!nodeModel.position) {
        calculatePosition(nodeModel);
    }
    var nodeKey = nodeModel.id;
    nodes[nodeKey] = nodeModel;
    nodes[nodeKey].id = nodeKey;
};

function calculatePosition(nodeModel) {
    var newPosition = {top: 20, left: 20};
    if(!_.isEmpty(nodes)) {
        var start = _.min(nodes,function (node) {
            return node.position.top;
        });
        var startPos = start.position;
        var tops = _.filter(nodes, function(node) {
            return node.position.top <= startPos.top + 60; //
        }) || newPosition;
        var mostRight = _.max(tops,function (node) {
            return node.position.left;
        }) || newPosition;

        var mostRightPos = mostRight.position;
        newPosition = {top: startPos.top, left: mostRightPos.left + 100};
    }
    nodeModel.position = newPosition;
}


var addNode = function(nodeElement, isSource, hasBooleanChoice, isMeasurementNode) {
    drawingArea.append(nodeElement);
    makeReadyForConnection(nodeElement, isSource, hasBooleanChoice, isMeasurementNode); //Can only be called after element is added
    updatePosition(nodeElement).call();
};
var yesSourceEndPoint = {
    endpoint:"Dot",
    isSource:true,
    overlays:[
        [ "Label", {
            location:[-0.4, 1.0],
            label:"Ja"
        } ]
    ],
    parameters: {
        choiceValue: "true",
        type: "choice",
        severity: ""
    }

};

var sourceEndPoint = {
    endpoint:"Dot",
    isSource:true,
    parameters: {
        severity: "",
        type: "normal"
    }

};

var skipMeasurementEndPoint = {
    endpoint: "Dot",
    isSource: true,
    overlays: [
        [ "Label", {
            location: [1.7, 1.0],
            label: "Udelad"
        } ]
    ],
    parameters: {
        measurementSkipped: "true",
        type: "measurementSkipped",
        severity: ""

    }
};

var noSourceEndPoint = {
    endpoint:"Dot",
    isSource:true,
    overlays:[
        [ "Label", {
            location:[1.7, 1.0],
            label:"Nej"
        } ]
    ],
    parameters: {
      choiceValue: "false",
        type: "choice",
        severity: ""

    }

};

var elementHasChoiceValueFalseEndpoint = function (element) {
    return getChoiceValueFalseEndpoint(element);
};

var elementHasChoiceValueTrueEndpoint = function (element) {
    return getChoiceValueTrueEndpoint(element);
};

var getSkipMeasurementEndpoint = function (element) {
    var endPoints = jsPlumb.getEndpoints(element);
    if (!endPoints) {
        return false;
    }
    var skipMeasurementEndpoint = undefined;
    $.each(endPoints, function (index, endpoint) {
        if (endpoint.getParameter('measurementSkipped') === "true") {
            skipMeasurementEndpoint = endpoint;
        }
    });

    return skipMeasurementEndpoint;
};

var getNormalEndpoint = function (element) {
    var endPoints = jsPlumb.getEndpoints(element);
    if (!endPoints) {
        return false;
    }
    var normalEndpoint = undefined;
    $.each(endPoints, function (index, endpoint) {
        if (endpoint.getParameter('type') === "normal") {
            //console.log("Found normal endpoint");
            normalEndpoint = endpoint;
        }
    });

    return normalEndpoint;
};


var getChoiceValueFalseEndpoint = function (element) {
    var endPoints = jsPlumb.getEndpoints(element);
    if (!endPoints) {
        return false;
    }
    var falseValueEndpoint;
    $.each(endPoints, function (index, endpoint) {
        if (endpoint.getParameter('choiceValue') === "false") {
            falseValueEndpoint = endpoint;
        }
    });

    return  falseValueEndpoint;
};

var getChoiceValueTrueEndpoint = function (element) {
    var endPoints = jsPlumb.getEndpoints(element);
    if (!endPoints) {
        return false;
    }
    var trueValueEndpoint;
    $.each(endPoints, function (index, endpoint) {
        if (endpoint.getParameter('choiceValue') === "true") {
            trueValueEndpoint = endpoint;
        }
    });

    return  trueValueEndpoint;
};


var makeReadyForConnection = function(element, makeAsSource, hasBooleanChoice, isMeasurementNode) {
    //console.log("-----",element,makeAsSource,hasBooleanChoice,isMeasurementNode);
    if(hasBooleanChoice) {
        //©©console.log("Is Boolean");
        if(!elementHasChoiceValueTrueEndpoint(element)) {
            jsPlumb.addEndpoint(element, yesSourceEndPoint, {isSource: makeAsSource, anchor:"BottomLeft"});
        }
        if(!elementHasChoiceValueFalseEndpoint(element)) {
            jsPlumb.addEndpoint(element, noSourceEndPoint, {isSource: makeAsSource, anchor:"BottomRight"});
        }
    } else if(isMeasurementNode) {
        //console.log("Is measurement node!");
        if(!getNormalEndpoint()) {
            //console.log("Adding normal endpoint");
            jsPlumb.addEndpoint(element, sourceEndPoint, {isSource: makeAsSource, anchor:"BottomLeft"});
        }
        if(!getSkipMeasurementEndpoint()) {
            //console.log("Adding skip endpoint");
            jsPlumb.addEndpoint(element, skipMeasurementEndPoint, {isSource: skipMeasurementEndPoint, anchor:"BottomRight"});
        }

    } else if(makeAsSource) {
        //console.log("Adding normal endpoint");
        jsPlumb.addEndpoint(element, sourceEndPoint, {isSource: makeAsSource});
    }

    jsPlumb.makeTarget(element);
    //console.log("-----");
};


function removeNodeAndConnections(node) {
    jsPlumb.detachAllConnections(node);
    jsPlumb.removeAllEndpoints(node);

    $(node).remove();
}

var addDeleteAction = function(node) {
    var deleteIcon = $("<i class='icon-trash deleteIcon'></i>");
    node.append(deleteIcon);
    //console.debug("Attach delete:",node)
    $(deleteIcon).click(function() {
        var connections = jsPlumb.select({target:node});
        removeNodeAndConnections(node);
        connections.each(function(connection){
            if(nodes[connection.source.attr('id')].dataType === 'BOOLEAN') {
                makeReadyForConnection(connection.source, true, true);
            } else {
                makeReadyForConnection(connection.source, true);
            }
        });

        var nodeModel = nodes[node.attr('id')];
        if(nodeModel.type === "start") {
            enableAddStartNode()
        }
        if(nodeModel.type === "end") {
            enableAddEndNode()
        }
        delete nodes[$(node).attr('id')];
    });
};

var addEditAction = function(node) {
    var editIcon = $("<i class='icon-cogs editIcon'></i>");
    node.append(editIcon);

    $(editIcon).click(function() {
        editNode(node);
    });
};

var editTextNode = function(nodeModel, node) {
    var modalElement = $('#createTextNodeModal');
    var headline = modalElement.find('#headline');
    var text = modalElement.find('#text');

    headline.val(nodeModel.headline);
    text.val(nodeModel.text);

    handleModal($(modalElement), function() {
        nodeModel.headline = headline.val();
        nodeModel.text = text.val();
        setNodeContents($(node), nodeModel.text);
        jsPlumb.repaint(node);
    }).call();
};

var editMeasurementNode = function(nodeModel, node) {
    var modalElement = $('#createMeasurementNodeModal');
    var headline = modalElement.find("#headline");
    var measurementType = modalElement.find("#measurementType");
    var shortText = modalElement.find("#shortText");

    modalElement.find("#" + nodeModel.measurementForm).prop('checked', true);
    measurementType.val(nodeModel.measurementType);
    headline.val(nodeModel.headline);
    shortText.val(nodeModel.shortText);

    handleModal($(modalElement), function() {
        nodeModel.measurementForm = modalElement.find('input[name=measurementForm]:checked').val();
        nodeModel.headline = headline.val();
        nodeModel.measurementType = measurementType.val();
        nodeModel.shortText = shortText.val();

        setNodeContents($(node), $('#createMeasurementNodeModal').find('#measurementType [value=' + nodeModel.measurementType + ']').text());
        jsPlumb.repaint(node);
    }).call();
};

var editInputNode = function(nodeModel, node) {

    var modalElement = $('#createInputNodeModal');
    var question = modalElement.find("#question");
    var dataType = modalElement.find("#dataType");
    var shortText = modalElement.find("#shortText");

    question.val(nodeModel.question);
    dataType.val(nodeModel.dataType);
    shortText.val(nodeModel.shortText);

    handleModal($(modalElement), function() {
        nodeModel.question = question.val();
        nodeModel.dataType = dataType.val();
        nodeModel.shortText = shortText.val();
        setNodeContents($(node), nodeModel.shortText);
        jsPlumb.repaint(node);
    }).call();
};

var editDelayNode = function(nodeModel, node) {

    var modalElement = $('#createDelayNodeModal');
    var text = modalElement.find("#text");
    var countType = modalElement.find("#countType");
    var shortText = modalElement.find("#shortText");
    var countTime = modalElement.find("#countTime");

    text.val(nodeModel.text);
    countType.val(nodeModel.countType);
    shortText.val(nodeModel.shortText);
    countTime.val(nodeModel.countTime);

    handleModal($(modalElement), function() {
        nodeModel.text = text.val();
        nodeModel.countType = countType.val();
        nodeModel.shortText = shortText.val();
        nodeModel.countTime = countTime.val();
        setNodeContents($(node), nodeModel.shortText);
        jsPlumb.repaint(node);
    }).call();
};

var editNode = function(node) {
    var nodeModel = nodes[$(node).attr('id')];

    switch(nodeModel.type) {
        case 'text':
            editTextNode(nodeModel, node);
            break;
        case 'input':
            editInputNode(nodeModel, node);
            break;
        case 'measurement':
            editMeasurementNode(nodeModel, node);
            break;
        case 'delay':
            editDelayNode(nodeModel, node);
            break;
    }
};

var setNodeContents = function(node, contents) {
    $(node).find("#content").attr('data-tooltip',contents).text(contents);
};

var baseNodeBuilder = function(icon, contents, idPrefix, skipEditAction, model) {
    var node =  $("<div><span id='content'></span></spand></script></div>");
    node.addClass("node");
    addLargeIcon(node, icon);

    setNodeContents(node, contents);
    setId(node, idPrefix, model);
    makeDraggable(node);
    addDeleteAction(node);

    if(!skipEditAction) {
        addEditAction(node);
    }

    return node;
};

var nextId = function() {
    return ++lastId;
};

var addLargeIcon = function(element, iconName) {
    var iconAdded = addIcon(element, iconName);
    $(iconAdded).addClass("icon-2x");

    return iconAdded;
};

var addIcon = function(element, iconName) {
    element.append($("<i class='icon-" + iconName + "' style='float:left;'></i>"));
};

var makeDraggable = function(element) {
    jsPlumb.draggable(element, {containment:"parent", stop: updatePosition(element)});
};

var updatePosition = function(element) {
    return function() {
        var node = nodes[$(element).attr('id')];
        if(node) {
            node.position = $(element).position();
        }
    };
};

var setId = function(element, prefix, model) {
    if(model) {
        //Get node numeric ID from node ID string
        var numberPattern = /\d+/g;
        var idMatches = model.id.match( numberPattern )
        var nodeID
        //If ID-strings are as expected, that is as this editor makes them,
        // the regexp result should be an array with one element.
        if(idMatches != null) {
            if (idMatches.length > 1) {
                //More than one number sequence in ID-string. ID-string not as
                // expected: Log warning and assume last number sequence is ID.
                console.log("QUESTIONNAIRE EDITOR - WARNING: Found an ID-string that did not look as expected!")
            }
            nodeID = idMatches[0]

            //Check if the current 'lastId' is numerically higher than this nodes numeric ID
            //otherwise set lastId to this nodes ID + 1 (this wont effect the rest of the import,
            //as imported nodes always get their own original ID)
            if (lastId <= nodeID) {
//                console.log("found node id larger than last ID. bumping "+lastId+" to "+nodeID);
                lastId = nodeID++;
            }
        }
    }
    element.attr({
        id: "" + prefix + nextId()
    });
};

var handleModal = function(modal, onSucess, beforeOpening) {
    return function() {
        if(beforeOpening) {
            beforeOpening();
        }

        modal.modal('show');

        $(modal).on('hide', function() {
            $(modal).find('#create').unbind();
            $(modal).find('#cancel').unbind();
        });

        $(modal).find('#cancel').click(function() {
            modal.modal('hide');
        });

        $(modal).find('#create').click(function() {
            onSucess(null);
            modal.modal('hide');
        });
    };
};



var serializeConnections = function() {
    return jsPlumb.getConnections().map(function(connection) {
        return {
            source:connection.sourceId,
            target:connection.targetId,
            choiceValue: connection.getParameter('choiceValue'),
            severity: connection.getParameter('severity'),
            measurementSkipped: connection.getParameter('measurementSkipped'),
            type: connection.getParameter('type')
        };
    });
};

var serializeQuestionnaire = function() {
    return {
        title: $('#title').val(),
        questionnaireHeaderId: getQuestionnaireHeaderId(),
        nodes: nodes,
        connections: serializeConnections(),
        standardSchedule: standardScheduleViewModel.json()
    };
};

function getQuestionnaireHeaderId() {
    return parseInt($('input[name="questionnaireHeaderId"]').val())
}

function getQuestionnaireId() {
    var questionnaireId = $('input[name="questionnaireId"]').val();
    return questionnaireId ? parseInt(questionnaireId) : undefined
}

var exitOnSave = false;

var saveError = function(jqXHR) {
    return function() {
        $('body').prepend("<div class='alert alert-error span12'><button type='button' class='close' data-dismiss='alert'>&times;</button> Der kunne ikke gemmes: " + jqXHR.responseText + "</div>");
        exitOnSave = false;
    };
};
var saveSucceded = function(jqXHR) {
    return function() {
        $('body').prepend("<div class='alert alert-info span12' style='position: absolute; z-index:100;' ><button type='button' class='close' data-dismiss='alert'>&times;</button> Skema gemt</div>");
        if(exitOnSave) {
            window.location.href = $('input[name="exitUrl"]').val()
        }
    };
};



var saveQuestionnaire = function() {
    var spinner = $('<i class="icon-refresh icon-spin"></i>');
    var saveButton =  $('#menu_save','#menu_save_on_exit');
    saveButton.button('loading');
    saveButton.prepend(spinner);

    var serializedQuestionnaire = serializeQuestionnaire();
    var url=$('input[name="saveUrl"]').val();
    var jqXHR = $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(serializedQuestionnaire),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    });
    jqXHR
        .always(function() {
            $('#menu_save i').remove();
            saveButton.button('reset');
        })
        .done(saveSucceded(jqXHR))
        .fail(saveError(jqXHR));

};

var saveQuestionnaireAndExit = function() {
    exitOnSave = true;
    saveQuestionnaire()
};

var updateSaveButton = function () {
    var createButton = $('#menu_save');
    if (($("#top_menu").find("#title").val().length > 0)) {
        createButton.removeClass('disabled');
        createButton.removeAttr('disabled');
    } else {
        createButton.addClass('disabled');
        createButton.attr('disabled', true);
    }
};

var inputValidation = function() {
    $(new function() {
        updateSaveButton();
        $('#top_menu').find('input').keyup(updateSaveButton);
    });
};

var isShowing = function() {
    return getQuestionnaireId()
};

var failedToDoAjaxCall = function (error) {
    $('body').prepend("<div class='alert alert-error span12'><button type='button' class='close' data-dismiss='alert'>&times;</button> Der kunne ikke gemmes: " + error + "</div>");
};

var failedToLoadModel = function(jqXHR) {
    return function() {
        failedToDoAjaxCall(jqXHR.responseText)
    };
};

var setPosition = function(element, position) {
    if(position) {
        $(element).css({
            top: position.top,
            left: position.left
        });
    }
};

var drawNodeFromModel = function(__, nodeModel) {
    switch(nodeModel.type) {
        case "start":
            addStartNode(nodeModel);
            break;
        case "end":
            addEndNode(nodeModel);
            break;
        case "measurement":
            addMeasurementNode(nodeModel);
            break;
        case "text":
            addTextNode(nodeModel);
            break;
        case "input":
            addInputNode(nodeModel);
            break;
        case "delay":
            addDelayNode(nodeModel);
    }
};

var drawNodesFromModel = function() {
    $.each(nodes, drawNodeFromModel);
};

var createConnection = function (__, connection) {
    var jsPlumbConnection;
    if(connection.choiceValue === "false") {
        jsPlumbConnection = jsPlumb.connect( {
                source: getChoiceValueFalseEndpoint(connection.source),
                target: connection.target
            }
        );
    } else if(connection.choiceValue === "true") {
        jsPlumbConnection = jsPlumb.connect( {
                source: getChoiceValueTrueEndpoint(connection.source),
                target: connection.target
            }
        );

    } else if(connection.measurementSkipped === "true") {
        jsPlumbConnection = jsPlumb.connect( {
                source: getSkipMeasurementEndpoint(connection.source),
                target: connection.target
            }
        );
    } else {
        jsPlumbConnection = jsPlumb.connect( {
                source: getNormalEndpoint(connection.source),
                target: connection.target
            }
        );
    }
    jsPlumbConnection.setParameter("severity", connection.severity);
    setConnectionColor(connection.severity, jsPlumbConnection);
};

var createConnections = function(connections) {
    $.each(connections, createConnection);
};

var restoreState = function(request) {
    return function() {
        restoreStateFromJsonString(request.responseText)();
    }
};

var loadModel = function() {
    $("#loadingModal").modal('show');
    var questionnaireHeaderId = getQuestionnaireHeaderId();
    var questionnaireId = getQuestionnaireId() || -1;
    var url = $('input[name="editorStateUrl"]').val();

    $.ajax({
        url: url,
        data: {id: questionnaireHeaderId, baseId: questionnaireId},
        success: function(data) {
            updateModel(data)
        },
        error: function(response, error) {
            failedToDoAjaxCall(error)
        },
        complete: function() {
            $("#loadingModal").modal('hide')
        }
    })
};


var addTextNode = function(model)  {
        var node;
        if(!model) {
            var headline = $('#createTextNodeModal').find('#headline').val();
            var text = $('#createTextNodeModal').find('#text').val();
            node = baseNodeBuilder('comment', text, "text", false, null);

            model = {
                type: 'text',
                headline: headline,
                text: text,
                id: $(node).attr('id')
            };
            addNodeToModel(model);
        } else {
            node = baseNodeBuilder('comment', model.text, "text", false, model);
            $(node).attr('id', model.id);
        }
        setPosition(node, model.position);
        addNode(node, true);
        updatePosition(node).call();
};

var addInputNode = function(model)  {
        var node;
        if(!model) {
            var $createInputNodeModal = $('#createInputNodeModal');
            var question = $createInputNodeModal.find('#question').val();
            var dataType = $createInputNodeModal.find('#dataType').val();
            var shortText = $createInputNodeModal.find('#shortText').val();

            node = baseNodeBuilder('question-sign', shortText, "input", false, null);
            model = {
                type: 'input',
                question: question,
                dataType: dataType,
                shortText: shortText,
                id: $(node).attr('id')
            };
            addNodeToModel(model);
        } else {
            node = baseNodeBuilder('question-sign', model.shortText, "input", false, model);
            $(node).attr('id', model.id);
        }
        setPosition(node, model.position);
        if(model.dataType === 'BOOLEAN') {
            addNode(node, true, true);
        } else {
            addNode(node, true, false);
        }
        updatePosition(node).call();

};

var addDelayNode = function(model)  {
        var node;
        if(!model) {
            var createDelayNodeModal = $('#createDelayNodeModal');
            var text = createDelayNodeModal.find('#text').val();
            var countType = createDelayNodeModal.find('#countType').val();
            var shortText = createDelayNodeModal.find('#shortText').val();
            var countTime = createDelayNodeModal.find('#countTime').val();

            node = baseNodeBuilder('time', shortText, "delay", false, null);
            model = {
                type: 'delay',
                countType: countType,
                shortText: shortText,
                text: text,
                countTime: countTime,
                id: $(node).attr('id')
            };
            addNodeToModel(model);
        } else {
            node = baseNodeBuilder('time', model.shortText, "delay", false, model);
            $(node).attr('id', model.id);
        }
        setPosition(node, model.position);
        addNode(node, true, false);
        updatePosition(node).call();
};

var addMeasurementNode = function(model)  {
        var node;
        var createMeasurementNodeModal = $('#createMeasurementNodeModal');
        if(!model) {
            var measurementMethod = $('input[name=measurementForm]:checked', createMeasurementNodeModal).val();
            var measurementType = createMeasurementNodeModal.find('#measurementType').val();
            var measurementTypeText = createMeasurementNodeModal.find('#measurementType :selected').text();
            var headline = createMeasurementNodeModal.find('#headline').val();
            var shotText = createMeasurementNodeModal.find('#shortText').val();

            node = baseNodeBuilder('beaker', measurementTypeText, "measurement", false, null);

            model = {
                type: 'measurement',
                measurementType: measurementType,
                measurementForm: measurementMethod,
                headline: headline,
                shortText: shotText,
                id: $(node).attr('id')
            };
            addNodeToModel(model);
        } else {
            node = baseNodeBuilder('beaker', model.shortText, "measurement", false, model);
            $(node).attr('id', model.id);
        }
        setPosition(node, model.position);
        addNode(node, true, false, true);
};

var addStartNode = function(model) {
        var node = baseNodeBuilder('circle-blank', ' Start', 'start', true, null);
        if(!model) {
            model = {type: 'start', id: $(node).attr('id')};
            addNodeToModel(model);
        } else {
            $(node).attr('id', model.id);
        }
        setPosition(node, model.position);
        addNode(node, true);
        $('#menu_add_node_start').addClass('disabled-link').unbind('click');
};

var addEndNode = function(model) {
        var node = baseNodeBuilder('circle', ' Slut', 'end', true, model);
        if(!model) {
            model = {type: 'end',id: $(node).attr('id')};
            addNodeToModel(model);
        } else {
            $(node).attr('id', model.id);
        }
        setPosition(node, model.position);
        addNode(node, false);
        $('#menu_add_node_end').addClass('disabled-link').unbind('click');

};

var setQuestionnaireExportText = function() {
    $('#export_questionnaire_modal').find('#questionnaire_json').val(JSON.stringify(serializeQuestionnaire()))
};

function removeAllNodes() {
    _.each(nodes, function(node) {
        removeNodeAndConnections($('#'+node.id));
    })
}
var updateModel = function(model) {
    //console.log("model:", model);
    removeAllNodes();
    nodes = model.nodes;
    drawNodesFromModel();
    createConnections(model.connections);
    schemaVersion = model.version;
    standardScheduleViewModel.fromJson(model.standardSchedule);
    $('#title').val(model.title);
    updateSaveButton();
};

var restoreStateFromJsonString = function(jsonString) {
    return function() {
        try {
            var model = $.parseJSON(jsonString);
            updateModel(model);
        } catch (e) {
            alert("Import fejlede.");
            return;
        }
    }
};

var updateConnectionSeverity = function (connection) {
    return function () {
        var modal = $("#set_connection_severity_modal");
        var severity = modal.find("input[name=severity]:checked").val();

        connection.setParameter("severity", severity);

        //console.log(connection.getParameters());

        setConnectionColor(severity, connection);
    }
};

var setConnectionColor = function (severity, connection) {
    if (severity === "YELLOW") {
        connection.setPaintStyle({lineWidth: 3, strokeStyle: '#ffd91b'});
    } else if (severity === "RED") {
        connection.setPaintStyle({lineWidth: 3, strokeStyle: '#cc0022'});
    } else {
        connection.setPaintStyle({lineWidth: 3, strokeStyle: '#0088cc'});
    }
};

function enableAddStartNode() {
    $('#menu_add_node_start').removeClass('disabled-link').click(function() {addStartNode()});
}

function enableAddEndNode() {
    $('#menu_add_node_end').removeClass('disabled-link').click(function() {addEndNode()});
}

<!-- Entry point -->
jsPlumb.ready(function() {
    setJsPlumbDefaults();
    drawingArea = $('#drawingArea');
    enableAddStartNode();
    $('#menu_add_node_measurement').click(handleModal($('#createMeasurementNodeModal'), addMeasurementNode));
    $('#menu_add_node_text').click(handleModal($('#createTextNodeModal'), addTextNode));
    $('#menu_add_node_input').click(handleModal($('#createInputNodeModal'), addInputNode));
    $('#menu_add_node_delay').click(handleModal($('#createDelayNodeModal'), addDelayNode));
    $('#export_questionnaire').click(handleModal($('#export_questionnaire_modal'), function(){}, setQuestionnaireExportText));
    enableAddEndNode();
    $('#import_questionnaire').click(handleModal($('#import_questionnaire_modal'), function(){restoreStateFromJsonString($('#import_questionnaire_modal').find('#questionnaire_json').val())()}));
    $('#menu_save').click(saveQuestionnaire);
    $('#menu_save_and_exit').click(saveQuestionnaireAndExit);
    $('#menu_exit').click(function() {
        window.location.href = $('input[name="exitUrl"]').val();
        return false;
    });
    inputValidation();

    if(isShowing()) {
        loadModel();
    }

    jsPlumb.bind("jsPlumbConnection", function(info) {
       info.connection.bind("click", function(connection){ handleModal($("#set_connection_severity_modal"), updateConnectionSeverity(connection))(); });
    });
    // Do not allow start to be attached to end node...
    jsPlumb.bind("beforeDrop", function(info) {
        if(info.sourceId == info.targetId) return false;
        if(info.targetId.indexOf("start") == 0) return false;
        return !(info.sourceId.indexOf("start") == 0 && info.targetId.indexOf("end") == 0);
    })

});




