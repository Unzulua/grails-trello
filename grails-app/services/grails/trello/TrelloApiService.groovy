package grails.trello

import grails.trello.domain.Board
import grails.trello.domain.BoardList
import grails.trello.domain.Card
import grails.trello.domain.Action
import grails.core.GrailsApplication
import groovyx.net.http.HttpBuilder

class TrelloApiService {

    GrailsApplication grailsApplication

    String BASE_URL = "https://api.trello.com"
    static String VERSION = "1"
    static String BOARDS = "boards"
    static String CARDS = "cards"

    List<Board> boards() {
        (List<Board>) query{ HttpBuilder builder, Map apiParams, Map config ->
            config.boards.collect { boardId ->
                Map boardParams = builder.get {
                    request.uri.path = "/${VERSION}/${BOARDS}/${boardId}"
                    request.uri.query = [
                        fields     : 'id,name',
                        lists      : 'open',
                        list_fields: 'id,name'
                    ] + apiParams
                }
                Board.from(boardParams)
            }
        }
    }

    List<Card> findAllCardsByDueDate(Date from, Date to = null){
        String fieldsToRetrieve =  'id,name,due,desc,idList,labels'

        (List<Card>) query{ HttpBuilder builder, Map apiParams, Map config ->
            config.boards.collect { boardId ->
                builder.get {
                    request.uri.path = "/${VERSION}/${BOARDS}/${boardId}/${CARDS}/open"
                    request.uri.query = [fields: fieldsToRetrieve] + apiParams
                }.collect{ params ->
                    Card card = Card.from(params)
                    if (card.expireBetween(from,to)){
                        return card
                    }
                    return null
                }.findAll{ it != null }
            }.flatten()
        }

    }

    BoardList findList(String id){
        if(id == null) {
            return BoardList.buildNull()
        }
        (BoardList) query{ HttpBuilder builder, Map apiParams, Map config ->
                def response = builder.get {
                    request.uri.path = "/${VERSION}/lists/${id}"
                    request.uri.query = apiParams
                }
                BoardList.from(response)
            }
    }

    List<Action> retrieveActions(Integer limit){
        (List<Action>) query{ HttpBuilder builder, Map apiParams, Map config ->
            def response = config.boards.collect { boardId ->
                builder.get {
                    request.uri.path = "/${VERSION}/${BOARDS}/${boardId}/actions"
                    request.uri.query = [
                         limit : limit
                     ] + apiParams
                }
            }.flatten()
            Action.fromList(response)
        }
    }

    private query(Closure closure){
        Map config = grailsApplication.config.trello
        HttpBuilder builder = HttpBuilder.configure {
            request.uri = "${BASE_URL}"
            request.accept = 'application/json'
        }
        Map params = [
            key        : config.apiKey,
            token      : config.oauthToken
        ]
        closure.call(builder, params, config)
    }
}
