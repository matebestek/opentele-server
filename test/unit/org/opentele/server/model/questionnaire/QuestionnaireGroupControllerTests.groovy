package org.opentele.server.model.questionnaire
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(QuestionnaireGroupController)
@Mock(QuestionnaireGroup)
class QuestionnaireGroupControllerTests {


    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/questionnaireGroup/list" == response.redirectedUrl
    }
/*
    void testList() {

        def model = controller.list()

        assert model.questionnaireGroupInstanceList.size() == 0
        assert model.questionnaireGroupInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.questionnaireGroupInstance != null
    }

    void testSave() {
        controller.save()

        assert model.questionnaireGroupInstance != null
        assert view == '/questionnaireGroup/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/questionnaireGroup/show/1'
        assert controller.flash.message != null
        assert QuestionnaireGroup.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup/list'


        populateValidParams(params)
        def questionnaireGroup = new QuestionnaireGroup(params)

        assert questionnaireGroup.save() != null

        params.id = questionnaireGroup.id

        def model = controller.show()

        assert model.questionnaireGroupInstance == questionnaireGroup
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup/list'


        populateValidParams(params)
        def questionnaireGroup = new QuestionnaireGroup(params)

        assert questionnaireGroup.save() != null

        params.id = questionnaireGroup.id

        def model = controller.edit()

        assert model.questionnaireGroupInstance == questionnaireGroup
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup/list'

        response.reset()


        populateValidParams(params)
        def questionnaireGroup = new QuestionnaireGroup(params)

        assert questionnaireGroup.save() != null

        // test invalid parameters in update
        params.id = questionnaireGroup.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/questionnaireGroup/edit"
        assert model.questionnaireGroupInstance != null

        questionnaireGroup.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/questionnaireGroup/show/$questionnaireGroup.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        questionnaireGroup.clearErrors()

        populateValidParams(params)
        params.id = questionnaireGroup.id
        params.version = -1
        controller.update()

        assert view == "/questionnaireGroup/edit"
        assert model.questionnaireGroupInstance != null
        assert model.questionnaireGroupInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup/list'

        response.reset()

        populateValidParams(params)
        def questionnaireGroup = new QuestionnaireGroup(params)

        assert questionnaireGroup.save() != null
        assert QuestionnaireGroup.count() == 1

        params.id = questionnaireGroup.id

        controller.delete()

        assert QuestionnaireGroup.count() == 0
        assert QuestionnaireGroup.get(questionnaireGroup.id) == null
        assert response.redirectedUrl == '/questionnaireGroup/list'
    }
    */
}
