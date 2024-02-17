package com.github.bkmbigo.composedecompiler.presentation.components.codeeditor

import androidx.compose.runtime.Stable

@Stable
class CodeEditorState constructor() {
    var code: String = ""
        private set

    constructor(code: String) : this() {
        this.code = code
    }

    fun onCodeChange(code: String) {
        this.code = code
    }

}
