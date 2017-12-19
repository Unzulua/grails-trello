package com.foozulu.domain

class Board {

    String id
    String name
    Set<BoardList> lists

    static Board from(Map params) {
        Board board = new Board(params)
        board.lists = params.lists.collect{ new BoardList(it) }
        board
    }

    def propertyMissing(name, value) {
        // nothing
    }
}
