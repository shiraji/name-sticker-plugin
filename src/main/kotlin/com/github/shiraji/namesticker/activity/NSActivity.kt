package com.github.shiraji.namesticker.activity

import com.github.shiraji.namesticker.listener.NSWindowFocusListener
import com.github.shiraji.namesticker.service.NSDisposableService
import com.github.shiraji.namesticker.service.NSUIService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer

class NSActivity : StartupActivity {
    private lateinit var service: NSUIService
    private lateinit var listener: NSWindowFocusListener

    override fun runActivity(project: Project) {
        service = NSUIService(project)
        Disposer.register(NSDisposableService.getInstance(project), service)

        listener = NSWindowFocusListener(project, service)
        service.addWindowFocusListener(listener)
        Disposer.register(NSDisposableService.getInstance(project), listener)
    }
}