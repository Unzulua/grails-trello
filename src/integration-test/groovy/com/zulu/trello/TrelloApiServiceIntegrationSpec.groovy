package com.zulu.trello

import com.foozulu.domain.Board
import com.foozulu.domain.Card
import grails.core.GrailsApplication
import grails.testing.mixin.integration.Integration
import spock.lang.Specification
import spock.lang.Unroll

@Integration
/**
 * This test is based on board 'for testing purposes'
 */
class TrelloApiServiceIntegrationSpec extends Specification{

    def "boards should be able to get all boards"(){
        when:
        List<Board> boards = trelloApiService.boards()

        then:
        boards*.name == ['for testing purposes']
        boards*.lists.name.flatten().sort() == ['Doing', 'Done', 'First list', 'Second list']

    }

    @Unroll('findAllCardsByDueDate between #fromString and #toString should return cards #cardNames')
    def "findAllCardsByDueDate returns all cards filtered by due date"(){
        given:
        Date from = dateOf(fromString)
        Date to = dateOf(toString)

        when:
        List<Card> cards = trelloApiService.findAllCardsByDueDate(from, to)

        then:
        cards*.name.sort() == cardNames.sort()
        cards*.dueDate.count{ it != null } == cardNames.size()

        where:
        fromString   | toString     | cardNames
        '01-11-2017' | '10-03-2018' | ['With due date', 'Another one with due date']
        '01-11-2017' | null         | ['With due date', 'Another one with due date']
        '01-01-2018' | '10-03-2018' | ['Another one with due date']
        '01-01-2017' | '31-12-2017' | ['With due date']
        '01-11-2016' | '10-03-2017' | []
        '01-11-2018' | '10-03-2019' | []
        null         | '10-03-2019' | []
        null         | null         | []
    }

    GrailsApplication grailsApplication
    TrelloApiService trelloApiService

    def setup(){
        grailsApplication.config.trello.boards = ['0RkhOB6l']
    }

    private Date dateOf(String date){
        if (!date){
            return null
        }
        Date.parse('dd-MM-yyyy', date)
    }
}
