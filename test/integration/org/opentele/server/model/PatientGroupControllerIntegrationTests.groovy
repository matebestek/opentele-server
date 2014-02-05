package org.opentele.server.model
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.opentele.server.PatientGroupService
import org.opentele.server.model.types.MeasurementTypeName
import org.opentele.server.model.types.PatientState
import org.opentele.server.model.types.Sex

class PatientGroupControllerIntegrationTests extends AbstractControllerIntegrationTest  {
    //Injections
    def grailsApplication
    def patientGroupService

    //Globals
    def patientGroupController

    @Before
    void setUp() {
        super.setUp()

        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()

        patientGroupController = new PatientGroupController()
        patientGroupController.patientGroupService = patientGroupService
        authenticate 'HelleAndersen','HelleAndersen1'
    }

    @After
    void tearDown() {
        super.tearDown()
    }

//    @Test
//    void testCanRemovePatientGroups() {
//        //Patient p = Patient.findByFirstName("Nancy Ann") //Assume nancy
//
//        Patient p = new Patient(firstName:'Test',
//                lastName:'P.G. Remove',
//                cpr:'1231234234',
//                sex: Sex.FEMALE,
//                address:'Vej1',
//                postalCode:'8000',
//                city:'Aarhus C',
//                state: PatientState.ACTIVE)
//
//        p.save(failOnError: true)
//        assert p != null
//
//        // Add a single patientgroup
//        Department deptB = Department.findByName("Afdeling-B Test")
//
//        StandardThresholdSet st = new StandardThresholdSet()
//        st.save(failOnError:true)
//
//        PatientGroup pg = new PatientGroup(name: "RemoveGroup", department: deptB, standardThresholdSet: st)
//        pg.save(failOnError:true)
//        Patient2PatientGroup.link(p, pg)
//
//        p.refresh()
//        assert p.groups.size() == 1
//
//        patientController.response.reset()
//
//        //Execute
//        patientController.params.groupid = []
//        patientController.params.id = p.id
//        patientController.update()
//
//        //Check
//        p = Patient.findById(p.id)
//        assert p.groups.size() == 0
//    }

    void testDelete() {

        Patient p = new Patient(firstName:'Test',
                lastName:'P.G. Remove',
                cpr:'1231234234',
                sex: Sex.FEMALE,
                address:'Vej1',
                postalCode:'8000',
                city:'Aarhus C',
                state: PatientState.ACTIVE)

        p.save(failOnError: true)
        assert p != null

        // Add a single patientgroup
        Department deptB = Department.findByName("Afdeling-B Test")

        StandardThresholdSet st = new StandardThresholdSet()
        st.save(failOnError:true)

        PatientGroup pg = new PatientGroup(name: "RemoveGroup", department: deptB, standardThresholdSet: st)
        pg.save(failOnError:true)
        Patient2PatientGroup.link(p, pg)

        p.refresh()
        assert p.groups.size() == 1

        patientGroupController.response.reset()

        //Execute
        patientGroupController.params.id = pg.id
        patientGroupController.delete()

        //Check
        pg = PatientGroup.findById(pg.id)
        assert pg == null

        assert patientGroupController.response.redirectedUrl == '/patientGroup/list'
    }

}
