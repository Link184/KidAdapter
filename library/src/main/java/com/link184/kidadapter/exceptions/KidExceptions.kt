package com.link184.kidadapter.exceptions

open class KidException(message: String, cause: Throwable? = null) : Throwable(message, cause)

open class UndefinedLayout(message: String) : KidException(message)

open class ZeroViewTypes : KidException(
    "View types are not defined, at least one must been defined. Use withViewType() method"
)

open class UndeclaredTypeModification(modelType: Class<*>?) : KidException("Sorry but $modelType isn't declared as a view type. " +
        "You try to update non-existent view type, you can update only declared view types, " +
        "please declare view type with withViewType() on adapter creation time method before update it")

open class UndeclaredTag(tag: String) : KidException("There are no view types with tag = $tag. Tag should be explicitly " +
        "declared with dsl method \"withViewType(TAG, configuration)\"")