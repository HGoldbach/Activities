package br.goldbach.activities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class MainDispatcherRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(UnconfinedTestDispatcher())
                base?.evaluate()
                Dispatchers.resetMain()
            }

        }
    }

}
//class MainDispatcherRule(
//    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
//) : TestWatcher() {
//
//    override fun starting(description: Description?) {
//        super.starting(description)
//        Dispatchers.setMain(testDispatcher)
//    }
//
//    override fun finished(description: Description?) {
//        super.finished(description)
//        Dispatchers.resetMain()
//    }
//}