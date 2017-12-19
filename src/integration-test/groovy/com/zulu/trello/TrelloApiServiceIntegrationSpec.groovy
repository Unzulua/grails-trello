package com.zulu.trello

import com.foozulu.domain.Board
import grails.core.GrailsApplication
import grails.testing.mixin.integration.Integration
import grails.util.Holders
import spock.lang.Specification

@Integration
class TrelloApiServiceIntegrationSpec extends Specification{

    def "boards should be able to get all boards"(){
        given:
        grailsApplication.config.trello.boards = ['0RkhOB6l']

        when:
        List<Board> boards = trelloApiService.boards()

        then:
        boards*.name == ['for testing purposes']
        boards*.lists.name.flatten().sort() == ['Doing', 'First list', 'Second list']

    }

    GrailsApplication grailsApplication
    TrelloApiService trelloApiService
}
