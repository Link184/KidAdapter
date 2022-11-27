
![logo](https://github.com/Link184/KidAdapter/blob/master/logo.png)

# KidAdapter
RecyclerView adapter for kids.

A kotlin dsl mechanism to simplify and reduce boilerplate logic of a `RecyclerView.Adapter`. 

With KidAdapter you can use all power of kotlin dsl to avoid wired standard implementation of `RecyclerView.Adapter` 
view types. You can easily maintain, update, move, swap, remove or add new view types using dsl code blocks. 

Bonus: Almost all logic by default works with `DiffUtil`

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.link184/kid-adapter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.link184/kid-adapter)
[![CircleCI](https://circleci.com/gh/Link184/KidAdapter/tree/master.svg?style=svg)](https://circleci.com/gh/Link184/KidAdapter/tree/master)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-KidAdapter-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7397)


Gradle
--------

Gradle:

```gradle
implementation 'com.link184:kid-adapter:1.3.9'
```

Samples
-----

Simple adapter without view types:
```kotlin
val adapter = recyclerView.setUp<MyObject> { //an extension on RecyclerView which return a instance of adapter
    // optional, set layout manager or leave to use default linear vertical
    withLayoutManager(GridLayoutManager(context, 3)) 
    // set layout res id for each adapter item
    withLayoutResId(R.layout.item_text) 
    // set adapter items
    withItems(mutableListOf(MyObject(name = "one"), MyObject("two"), MyObject("three")))
    // set bind action
    bind { item -> // this - adapter view holder itemView, item - current item
        this.setBackgroundColor(getRandomColor())
        stringName.text = item.name //string view is a synthetic inflated view from bind function context 
        setOnClickListener { ... } //click listener on ViewHolder.itemView
    }
    // if you need adapter position use this method instead of "bind"
    bindIndexed { item, index -> // item - current item, index - adapterPosition
        ...
    }
}

//runtime adapter update
adapter + MyObject("four")
adapter += mutableListOf(MyObject("1"), MyObject("2"))
adapter[2] = MyObject("two")
adapter - MyObject("four")
adapter + MyObject("five") + MyObject("six") - MyObject("one")
```

Adapter with view types:
```kotlin
val adapter = recyclerView.setUp {
    // declare a viewtype
    withViewType("FIRST_STRING_TAG") { // tag is optional but is useful for future updates when you have multiple view typs with the same item types
        // optional, set layout manager or leave to use default linear vertical
        withLayoutManager { GridLayoutManager(context, 3) }
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
            setOnClickListener { ... } //click listener on ViewHolder.itemView
        }
        // if you need adapter position use this method instead of "bind"
        bindIndexed { item, index -> // item - current item, index - adapterPosition
            ...
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
    adapter update { ... }
}
```

Update multiple view type adapter.

```kotlin
adapter update {
    insertBottom(mutableListOf("thirteen", "fourteen"), SECOND_STRING_TAG)
    insertTop(mutableListOf("asd", "asd")) // there are no tag, library automatically detect and insert items on first list of strings
    insert(2, mutableListOf(4, 5, 6, 7)) // no tag, items will be inserted in first list of integers
    removeItems(mutableListOf(1,3,6))
    removeItems(mutableListOf("one", "thirteen"))
    removeAll()
}

adapter restructure {
    insert(2, "tag") {
        withItems(items)
        withLayoutResId(android.R.layout.list_content)
        bind<Int> {
            println("We are here $it")
        }
    }
    insertTop("top1Tag") { ... }
    insert(3, "insertAt3Tag") { ... }
    insertBottom("bottom1Tag") { ... }
    replace("top1Tag") { ... }
    swap(0, 3)
    removeAll()
    insertTop("top1Tag") { ... }
}
```

Proguard
-------
Don't worry about that.

License
-------
See the [LICENSE][1] file for details.

[1]: https://github.com/Link184/KidAdapter/blob/master/LICENSE
