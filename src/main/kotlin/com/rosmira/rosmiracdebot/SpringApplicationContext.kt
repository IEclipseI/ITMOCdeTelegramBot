package com.rosmira.rosmiracdebot

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringApplicationContext : ApplicationContextAware {
    /**
     * This method is called from within the ApplicationContext once it is
     * done starting up, it will stick a reference to itself into this bean.
     * @param context a reference to the ApplicationContext.
     */
    override fun setApplicationContext(context: ApplicationContext) {
        CONTEXT = context
    }

    companion object {
        private lateinit var CONTEXT: ApplicationContext

        /**
         * This is about the same as context.getBean("beanName"), except it has its
         * own static handle to the Spring context, so calling this method statically
         * will give access to the beans by name in the Spring application context.
         * As in the context.getBean("beanName") call, the caller must cast to the
         * appropriate target class. If the bean does not exist, then a Runtime error
         * will be thrown.
         * @param beanName the name of the bean to get.
         * @return an Object reference to the named bean.
         */
        fun getContext(): ApplicationContext = CONTEXT

        fun getBean(beanName: String?): Any {
            return CONTEXT!!.getBean(beanName!!)
        }
    }
}