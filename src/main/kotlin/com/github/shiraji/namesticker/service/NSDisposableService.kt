package com.github.shiraji.namesticker.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class NSDisposableService : Disposable {
    companion object {
        fun getInstance(project: Project): NSDisposableService = project.getService(NSDisposableService::class.java)
    }

    override fun dispose() {
    }
}