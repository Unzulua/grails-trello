package grails.trello.domain

//[id:5a3940b214fd8aba24433229, name:First list, closed:false, idBoard:5a394064b1171cc47ccce67e, pos:65535]
class List {

  String id
  String name
  String original

  static List from(Map params) {
    new List(
      id: params.id,
      name: params.name,
      original: params
    )
  }

}
