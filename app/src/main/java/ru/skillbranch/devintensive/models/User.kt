package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User (
    val id:String,
    var firstName:String?,
    var lastName:String?,
    var avatar:String?,
    var rating:Int = 0,
    var respect:Int = 0,
    var lastVisit:Date? = null,
    var isOnline:Boolean = false
) {

    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, "John", "Doe")

    init {
        println("It's Alive!!! \n${if (lastName == "Doe") "His name is $firstName $lastName" else "And his name is $firstName $lastName!!!"}\n")
    }

    companion object Factory {
        var lastId : Int = -1
        fun makeUser(fullName: String?): User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullName)

            return User(id = "$lastId", firstName = firstName, lastName = lastName)
        }
    }

    //region ============================ Builder =================================
    class Builder
    {
        private var id:String? = null
        private var firstName:String?  = null
        private var lastName:String? = null
        private var avatar:String? = null
        private var rating:Int = 0
        private var respect:Int = 0
        private var lastVisit:Date? = null
        private var isOnline:Boolean = false

        fun id(arg:String):Builder {
            this.id = arg
            return this
        }
        fun firstName(arg:String):Builder {
            this.firstName = arg
            return this
        }
        fun lastName(arg:String):Builder {
            this.lastName = arg
            return this
        }
        fun avatar(arg:String):Builder {
            this.avatar = arg
            return this
        }
        fun rating(arg:Int):Builder {
            this.rating = arg
            return this
        }
        fun respect(arg:Int):Builder {
            this.respect = arg
            return this
        }
        fun lastVisit(arg:Date):Builder {
            this.lastVisit = arg
            return this
        }
        fun isOnline(arg:Boolean):Builder {
            this.isOnline = arg
            return this
        }
        fun build():User{
            val user = if(id == null) makeUser("${firstName ?: ""} ${lastName ?: ""}") else User(id!!,firstName,lastName)
            user.avatar = avatar
            user.rating = rating
            user.respect = respect
            user.lastVisit = lastVisit
            user.isOnline = isOnline
            return user
        }
    }
    //endregion ============================ Builder =================================
}

