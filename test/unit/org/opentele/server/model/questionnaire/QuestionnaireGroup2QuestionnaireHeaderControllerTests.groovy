package org.opentele.server.model.questionnaire



import org.junit.*
import grails.test.mixin.*

@TestFor(QuestionnaireGroup2QuestionnaireHeaderController)
@Mock(QuestionnaireGroup2QuestionnaireHeader)
@Ignore
class QuestionnaireGroup2QuestionnaireHeaderControllerTests {



    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/questionnaireGroup2QuestionnaireHeader/list" == response.redirectedUrl
    }

/*    void testList() {

        def model = controller.list()

        assert model.questionnaireGroup2QuestionnaireHeaderInstanceList.size() == 0
        assert model.questionnaireGroup2QuestionnaireHeaderInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.questionnaireGroup2QuestionnaireHeaderInstance != null
    }

    void testSave() {
        controller.save()

        assert model.questionnaireGroup2QuestionnaireHeaderInstance != null
        assert view == '/questionnaireGroup2QuestionnaireHeader/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/questionnaireGroup2QuestionnaireHeader/show/1'
        assert controller.flash.message != null
        assert QuestionnaireGroup2QuestionnaireHeader.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup2QuestionnaireHeader/list'


        populateValidParams(params)
        def questionnaireGroup2QuestionnaireHeader = new QuestionnaireGroup2QuestionnaireHeader(params)

        assert questionnaireGroup2QuestionnaireHeader.save() != null

        params.id = questionnaireGroup2QuestionnaireHeader.id

        def model = controller.show()

        assert model.questionnaireGroup2QuestionnaireHeaderInstance == questionnaireGroup2QuestionnaireHeader
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup2QuestionnaireHeader/list'


        populateValidParams(params)
        def questionnaireGroup2QuestionnaireHeader = new QuestionnaireGroup2QuestionnaireHeader(params)

        assert questionnaireGroup2QuestionnaireHeader.save() != null

        params.id = questionnaireGroup2QuestionnaireHeader.id

        def model = controller.edit()

        assert model.questionnaireGroup2QuestionnaireHeaderInstance == questionnaireGroup2QuestionnaireHeader
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup2QuestionnaireHeader/list'

        response.reset()


        populateValidParams(params)
        def questionnaireGroup2QuestionnaireHeader = new QuestionnaireGroup2QuestionnaireHeader(params)

        assert questionnaireGroup2QuestionnaireHeader.save() != null

        // test invalid parameters in update
        params.id = questionnaireGroup2QuestionnaireHeader.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/questionnaireGroup2QuestionnaireHeader/edit"
        assert model.questionnaireGroup2QuestionnaireHeaderInstance != null

        questionnaireGroup2QuestionnaireHeader.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/questionnaireGroup2QuestionnaireHeader/show/$questionnaireGroup2QuestionnaireHeader.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        questionnaireGroup2QuestionnaireHeader.clearErrors()

        populateValidParams(params)
        params.id = questionnaireGroup2QuestionnaireHeader.id
        params.version = -1
        controller.update()

        assert view == "/questionnaireGroup2QuestionnaireHeader/edit"
        assert model.questionnaireGroup2QuestionnaireHeaderInstance != null
        assert model.questionnaireGroup2QuestionnaireHeaderInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireGroup2QuestionnaireHeader/list'

        response.reset()

        populateValidParams(params)
        def questionnaireGroup2QuestionnaireHeader = new QuestionnaireGroup2QuestionnaireHeader(params)

        assert questionnaireGroup2QuestionnaireHeader.save() != null
        assert QuestionnaireGroup2QuestionnaireHeader.count() == 1

        params.id = questionnaireGroup2QuestionnaireHeader.id

        controller.delete()

        assert QuestionnaireGroup2QuestionnaireHeader.count() == 0
        assert QuestionnaireGroup2QuestionnaireHeader.get(questionnaireGroup2QuestionnaireHeader.id) == null
        assert response.redirectedUrl == '/questionnaireGroup2QuestionnaireHeader/list'
    }
*/
}
