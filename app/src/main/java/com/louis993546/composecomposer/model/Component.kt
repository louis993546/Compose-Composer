package com.louis993546.composecomposer.model

sealed class Component(val hasChildren: Boolean = false) {
    data class Text(val text: String): Component() {
        override fun meaningfulName() = "Text (${text})"
    }

    data class Row(val children: List<Component>): Component(hasChildren = true) {
        override fun meaningfulName() = "Row (${children.size})"
    }
    data class Column(val children: List<Component>): Component(hasChildren = true) {
        override fun meaningfulName() = "Column (${children.size})"
    }

    data class Button(val text: String): Component() {
        override fun meaningfulName() = "Button ($text)"
    }

    abstract fun meaningfulName(): String
}