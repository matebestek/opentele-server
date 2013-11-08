package org.opentele.server.model

import org.junit.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class MessageControllerTests extends AbstractControllerIntegrationTest {
	def bootStrapService
	def controller
    def grailsApplication
    
    @Before
	void setUp() {
		super.setUp()
        
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
        
		println "Setting up JSON marshallers"
		bootStrapService.registerCustomJSONMarshallers()
		
		controller = new MessageController()
		controller.response.format = "json"
	}

	@After
	void tearDown() {

	}

	@Test
	void testList() {
		Department d = Department.build()
        d.save(failOnError: true)
		
        User u = User.build(username:"toUser", password: "toUser12", enabled:true)
		u.save(failOnError: true)

        Patient p = Patient.build(user: u, cpr: "0102800102", thresholds: new ArrayList<Threshold>())
        p.save(failOnError: true)

        Message messageFromClinician = Message.build(department: d,patient: p)
		messageFromClinician.save(failOnError: true)
		Message messageFromPatient = Message.build(department: d,patient: p, sentByPatient: true)
		messageFromPatient.save(failOnError: true)

        authenticate 'toUser','toUser12'
		controller.list()

		def JSONresponse = controller.response.json

		assert JSONresponse.messages[0].to.id == p.id
		assert JSONresponse.messages[0].from.id == d.id

		//Should now return all messages
		assert JSONresponse.messages.length() == 2
	}

    @Test
    void testSendMessage() {
        def login = "Erna"

        authenticate login, 'abcd1234'

        def p = Patient.findByFirstName(login)

        // Build upload json message, containing results..
        def testData = [ "department" : p.id,
                "title":"Test message",
                "text": "This is a message"
        ]

        controller.request.json = testData
        controller.save()

        println "Response: " + controller.response.status
        assertTrue controller.response.status == 200
    }

	void testCreate() {
		def login = "testCreate"
        User.build(username: login, password: login + '1', enabled: true)

        def auth = new UsernamePasswordAuthenticationToken(login,login+'1')
		def authtoken = caseInsensitivePasswordAuthenticationProvider.authenticate(auth)
		SecurityContextHolder.getContext().setAuthentication(authtoken)

		
		def controller = new MessageController()
		def model = controller.create()

		assert model.messageInstance != null
	}
}
