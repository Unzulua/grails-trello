package grails.trello

import com.stehno.ersatz.ContentType
import com.stehno.ersatz.Encoders
import com.stehno.ersatz.ErsatzServer
import grails.testing.services.ServiceUnitTest
import grails.trello.domain.Action
import grails.trello.domain.ActionType
import grails.trello.domain.BoardList
import spock.lang.Specification

class TrelloApiServiceSpec extends Specification implements ServiceUnitTest<TrelloApiService> {

    def setup() {
        config.trello.boards = ['aBoardName']
        config.trello.date.format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }

    def "Retrieve last action from a board"() {
        given:
        Integer numberOfActions = 1
        List arrayOfActions = [[
                                   data: [
                                       list: [name: 'First list'],
                                       card: [name: 'With due date']
                                   ],
                                   date: '2017-12-21T10:14:27.060Z',
                                   type: 'updateCard',
                                   memberCreator: [username: 'jjballano']
                               ]]
        String uri = "/1/boards/${config.trello.boards.first()}/actions"

        service.BASE_URL = mockResponse(uri, arrayOfActions, ['limit': numberOfActions.toString()])
        when:
        Action action = service.retrieveActions(numberOfActions).first()

        then:
        action.cardName == 'With due date'
        action.user == 'jjballano'
        action.project == 'First list'
        action.date.toString() == 'Thu Dec 21 00:00:00 UTC 2017'
        action.type == ActionType.UPDATE_CARD
    }

    def "Filters actions of create and update card"() {
        given:
        Integer numberOfActions = 1
        List arrayOfActions = [[
                                   data: [list: [name: 'First list']],
                                   date: '2017-12-21T10:14:27.060Z',
                                   type: 'createList',
                                   memberCreator: [username: 'jjballano']
                               ]]
        String uri = "/1/boards/${config.trello.boards.first()}/actions"

        service.BASE_URL = mockResponse(uri, arrayOfActions, ['limit': numberOfActions.toString()])
        when:
        List<Action> actions = service.retrieveActions(numberOfActions)

        then:
        actions.size() == 0
    }

    def "Retrieves a list by its id"() {
        given:
        def aList = [
            "id": "5a3940b214fd8aba24433229",
            "name": "Albacete",
            "idBoard": "560bf4298b3dda300c18d09c"
        ]
        String uri = "/1/lists/${aList.id}"

        service.BASE_URL = mockResponse(uri, aList)
        when:
        BoardList list = service.findList("5a3940b214fd8aba24433229")

        then:
        list.name == "Albacete"
        list.id == aList.id
    }

    def "Returns an empty list if the id is empty"() {
        when:
        BoardList list = service.findList(null)

        then:
        list.name == ""
        list.id == ""
    }

    def "findAllCardsByDueDate returns all labels assigned to each particular card"(){
        given:
        List<Map> aList = [[labels: [[name:'test1'], [name:'test2']], due: '2017-06-08T13:07:57.302Z']]
        String uri = "/1/boards/${config.trello.boards.first()}/cards/open"
        service.BASE_URL = mockResponse(uri, aList, ['fields':'id,name,due,desc,idList,labels'])

        Date date = Date.parse('dd-MM-yyyy', '08-06-2017')

        expect:
        service.findAllCardsByDueDate(date - 10, date + 5)[0].labels == ['test1', 'test2']

    }


    private String mockResponse(String uri, Object response, Map params = [:]) {
        ErsatzServer ersatz = new ErsatzServer()
        ersatz.expectations {
            def mock = get(uri) {
                called(1)
                responder {
                    encoder(ContentType.APPLICATION_JSON, response.getClass(), Encoders.json)
                    code(200)
                    content(response, ContentType.APPLICATION_JSON)
                }
            }
            params?.keySet()?.each { key ->
                mock = mock.query(key, params[key])
            }
            mock
        }
        ersatz.httpUrl
    }
}
