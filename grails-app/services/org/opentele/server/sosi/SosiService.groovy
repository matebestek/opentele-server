package org.opentele.server.sosi

import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.opentele.server.dgks.monitoringdataset.version1_0_1.CreateMonitoringDataset
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.CreateMonitoringDatasetRequestMessage
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.ObjectFactory
import org.opentele.server.dgks.monitoringdataset.version1_0_1.generated.SelfMonitoredSampleType
import org.opentele.server.kihdb.RequestUtil
import org.opentele.server.util.SosiUtil
import wslite.soap.SOAPClient

class SosiService {
    SosiUtil sosiUtil
    String cvr
    String systemName

    //TODO mss check that this method is called
    void setGrailsApplication(GrailsApplication grailsApplication) {
        cvr = grailsApplication.config.seal.cvr
        systemName = grailsApplication.config.seal.systemName
        sosiUtil = new SosiUtil(grailsApplication.config)
    }

    def createRequest() {
        Request sosiRequest = createSosiRequest()
        setIDCard(sosiRequest)
        sosiRequest
    }

    private void setIDCard(Request sosiRequest) {
        IDCard signedIdCard = sosiUtil.getSignedIDCard()
        sosiRequest.setIDCard(signedIdCard)
    }

    private Request createSosiRequest() {
        SOSIFactory sosiFactory = sosiUtil.getSOSIFactory()
        sosiFactory.createNewRequest(false, "flow")
    }
}
