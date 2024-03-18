package org.example.kotlinspringplayground.batch

data class Person(var firstName: String, var lastName: String) {
    //    FlatFileItemReaderBuilder에서 no-arg 생성자를 통해 객체를 생성하기때문에 기본생성자를 선언한다..
    constructor() : this(
        "",
        ""
    )
}
