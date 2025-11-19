package sample

import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ResultHandler
import org.gradle.tooling.events.*
import org.gradle.tooling.events.test.*
import java.io.File

class M7ToolingClient {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Non-class-based tests will be reported to Tooling API (TAPI) clients like class-based tests.
            // Gradle will emit the same events/data:
            //     Test started/finished
            //     Test results (successful, failed, skipped)
            //     Test metadata (includes ReportEntry and FileEntry data)
            //     Test output

            println("------------------------------------------------")
            println("Running resource-based tests via the Tooling API")
            println("------------------------------------------------")
            val projectDir = File("..")
            GradleConnector.newConnector()
                .forProjectDirectory(projectDir)
                .useBuildDistribution()
                .connect()
                .use {
                    it.newTestLauncher()
                        .addProgressListener(ProgressEventListener(), OperationType.TEST, OperationType.TEST_OUTPUT, OperationType.TEST_METADATA)
                        .withTestsFor { s -> s.forTaskPath(":demo-m7:customTest")}
                        .run(SuppressingResultHandler)
                }
        }
    }

    class ProgressEventListener : ProgressListener {
        var descriptorParents = mutableMapOf<OperationDescriptor, Int>()
        override fun statusChanged(event: ProgressEvent) {
            val parents: Int
            if (event.descriptor.parent == null) {
                parents = 0
                descriptorParents[event.descriptor] = parents
            } else {
                val grandParents = descriptorParents[event.descriptor.parent]!!
                parents = grandParents + 1
                descriptorParents[event.descriptor] = parents
            }
            println("${"  ".repeat(parents)}${renderEvent(event)}")
        }

        private fun renderEvent(event: ProgressEvent): String {
            val prefix = when (event) {
                is StartEvent -> "START   "
                is FinishEvent -> {
                  when(event.result) {
                        is SuccessResult -> "SUCCESS  "
                        is SkippedResult->  "SKIPPED  "
                        is FailureResult -> "FAILURE  "
                        else -> "FINISH   "
                  }
                }
                is TestOutputEvent ->   "OUTPUT   "
                is TestMetadataEvent -> "METADATA "
                else -> "UNKNOWN "
            }

            val descriptor = event.descriptor
            val content = when (descriptor) {
                is SingleFileResourceBasedTestOperationDescriptor -> {
                    "$descriptor (file=${descriptor.file.absolutePath})"
                }
                is TestOutputDescriptor -> {
                    "${descriptor.destination}: ${descriptor.message.trim()}"
                }
                else -> {
                    if (event is TestKeyValueMetadataEvent) {
                        if (event.values.size == 1) "Entry:" + event.values.toString() else "Entries:" + event.values.toString()
                    } else if (event is TestFileAttachmentMetadataEvent) {
                        "File: ${event.file.absolutePath} type: ${event.mediaType}"
                    } else {
                        "$descriptor"
                    }
                }
            }

            val postfix = if (event is FinishEvent && event.result is FailureResult && (event.result as FailureResult).failures.isNotEmpty()) {
                "(reason: ${(event.result as FailureResult).failures[0].message})"
            } else {
                ""
            }

            return "$prefix $content $postfix"
        }

    }

    object SuppressingResultHandler : ResultHandler<Void> {
        override fun onComplete(result: Void?) {
        }

        override fun onFailure(failure: GradleConnectionException?) {
            // suppress build failure
        }
    }
}
