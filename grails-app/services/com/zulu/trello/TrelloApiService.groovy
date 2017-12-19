package com.zulu.trello

import com.foozulu.domain.Board
import grails.core.GrailsApplication
import groovyx.net.http.HttpBuilder

class TrelloApiService {

    GrailsApplication grailsApplication

    static String BASE_URL = "https://api.trello.com"
    static String VERSION = "1"
    static String BOARDS = "boards"

    List<Board> boards() {
        Map config = grailsApplication.config.trello
        HttpBuilder builder = HttpBuilder.configure {
            request.uri = "${BASE_URL}"
            request.accept = 'application/json'
        }
        config.boards.collect { board ->
            Map boardParams = builder.get {
                request.uri.path = "/${VERSION}/${BOARDS}/${board}"
                request.uri.query = [
                    fields     : 'id,name',
                    lists      : 'open',
                    list_fields: 'id,name',
                    key        : config.apiKey,
                    token      : config.oauthToken
                ]
            }
            Board.from(boardParams)
        }
    }
}
