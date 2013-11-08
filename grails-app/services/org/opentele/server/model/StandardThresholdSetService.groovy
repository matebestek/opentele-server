package org.opentele.server.model


class StandardThresholdSetService {
    StandardThresholdSet findStandardThresholdSetForThreshold(Threshold threshold) {
        StandardThresholdSet.forThreshold(threshold).get()
    }
}
