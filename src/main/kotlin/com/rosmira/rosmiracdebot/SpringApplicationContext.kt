package com.rosmira.rosmiracdebot

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class SpringApplicationContext : ApplicationContextAware {
    override fun setApplicationContext(context: ApplicationContext) {
        CONTEXT = context
    }

    companion object {
        private lateinit var CONTEXT: ApplicationContext

        fun getContext(): ApplicationContext = CONTEXT

        fun <T : Any> getBean(kClass: KClass<T>): T {
            return CONTEXT.getBean(kClass.java)
        }
    }
}