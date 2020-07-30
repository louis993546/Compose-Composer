package com.louis993546.composecomposer.model

sealed class Component(val hasChildren: Boolean = false) {
    data class Text(val text: String): Component() {
        override fun meaningfulName() = "Text (${text})"
    }

    data class Button(val text: String): Component() {
        override fun meaningfulName() = "Button ($text)"
    }

    abstract class NestedComponent : Component(hasChildren = true) {
        abstract val children: List<Component>
    }

    data class Row(override val children: List<Component>): NestedComponent() {
        override fun meaningfulName() = "Row (${children.size})"
    }
    data class Column(override val children: List<Component>): NestedComponent() {
        override fun meaningfulName() = "Column (${children.size})"
    }

    abstract fun meaningfulName(): String
}