
![logo](https://github.com/Link184/KidAdapter/blob/master/logo.png)

# KidAdapter
RecyclerView adapter for kids.

A kotlin dsl mechanism to simplify and reduce boilerplate logic of a RecyclerView.Adapter

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.link184/kid-adapter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.link184/kid-adapter)
[![Build Status](https://travis-ci.com/Link184/KidAdapter.svg?branch=master)](https://travis-ci.com/Link184/KidAdapter)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)


Gradle
--------

Gradle:

```gradle
implementation 'com.link184:kid-adapter:1.0.0'
```

Samples
-----

Simple adapter without view types:
```kotlin
val adapter = recyclerView.setUp<MyObject> { //an extension on RecyclerView which return a instance of adapter
    // optional, set layout manager or dont set and use default linear vertical
    withLayoutManager(GridLayoutManager(context, 3)) 
    // set layout res id for each adapter item
    withLayoutResId(R.layout.item_text) 
    // set adapter items
    withItems(mutableListOf(MyObject(name = "one"), MyObject("two"), MyObject("three")))
    // set bind action
    bind { item -> // this - adapter view holder itemView, item - current item
        (stringName.parent as ViewGroup).setBackgroundColor(getRandomColor())
        stringName.text = item.name //string view is a synthetic inflated view from bind function context 
    }
}

adapter + MyObject("four") //runtime adapter update

```

Adapter with view types:
```kotlin
val adapter = recyclerView.setUp {
    // declare a viewtype
    withViewType("FIRST_STRING_TAG") { // tag is optional but is useful to do future updates when you have multiple view typs with the same item types
        // set layout res id to current view type
        withLayoutResId(R.layout.item_text)
        // set items to currect view type
        withItems(mutableListOf("one", "two", "three", "four", "five", "six", "seven"))
        // optional, a callback from DiffUtils, by default it compare items with equals() method, set it if you need a custom behavior
        withContentComparator<String> { oldItem, newItem ->
            oldItem.length > newItem.length
        }
        // optional, a callback from DiffUtils, by default it compare items with equals() method, set it if you need a custom behavior
        withItemsComparator<Int> { oldItem, newItem -> 
            oldItem.hashCode() == newItem.hashCode()
        }
        // set bind action
        bind<String> { // this - is adapter view hoder itemView, item - current item
            stringName.text = it
        }
    }

    withViewType {
        withLayoutResId(R.layout.item_int)
        withItems(mutableListOf(1, 2, 3, 4, 5, 6))
        bind<Int> {
            intName.text = it.toString()
        }
    }


    withViewType("SECOND_STRING_TAG") {
        withLayoutResId(R.layout.item_text)
        withItems(mutableListOf("eight", "nine", "ten", "eleven", "twelve"))
        bind<String> {
            stringName.text = it
        }
    }
    
    //Update adapter as needed
    adapter.update { ... }
}
```

Update multiple view type adapter.

```kotlin
adapter.update {
    insertBottom(mutableListOf("thirteen", "fourteen"), SECOND_STRING_TAG)
    insertTop(mutableListOf("asd", "asd")) // there are no tag, library automatically detect ind insert items on first list of strings
    insert(2, mutableListOf(4, 5, 6, 7)) // not tag, items will be inserted in first list of ints
    removeItems(mutableListOf(1,3,6))
    removeItems(mutableListOf("one", "thirteen"))
    removeAll()
}
```

License
-------
See the [LICENSE][1] file for details.

[1]: https://github.com/Link184/Respiration/blob/master/LICENSE