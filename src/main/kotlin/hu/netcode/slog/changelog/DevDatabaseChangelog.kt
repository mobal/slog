package hu.netcode.slog.changelog

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate
import com.github.javafaker.Faker
import com.github.slugify.Slugify
import hu.netcode.slog.data.document.Meta
import hu.netcode.slog.data.document.Post
import hu.netcode.slog.data.document.User
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import java.time.LocalDateTime
import java.util.Locale

@ChangeLog(order = "001")
@Profile(value = ["dev"])
class DevDatabaseChangelog {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var faker: Faker = Faker(Locale("hu-HU"))
    private var slugify: Slugify = Slugify()

    @ChangeSet(
        author = "mobal",
        id = "Clean up collections",
        order = "001",
        runAlways = true
    )
    fun cleanUp(mongockTemplate: MongockTemplate) {
        logger.info("Drop collections")
        mongockTemplate.getCollection("posts").drop()
        mongockTemplate.getCollection("users").drop()
    }

    @ChangeSet(
        author = "mobal",
        id = "Create and initialize posts collection",
        order = "002",
        runAlways = true
    )
    fun createPostsCollection(mongockTemplate: MongockTemplate) {
        logger.info("Create posts collection")
        val postList = mutableListOf<Post>()
        for (i in 1..100) {
            postList.add(
                Post(
                    author = faker.book().author(),
                    body = faker.lorem().paragraph(),
                    meta = Meta(slugify.slugify(faker.book().title())),
                    tagList = faker.lorem().words(5),
                    title = faker.book().title(),
                    publishedAt = LocalDateTime.now()
                )
            )
        }
        mongockTemplate.insertAll(postList)
    }

    @ChangeSet(
        author = "mobal",
        id = "Create and initialize users collection",
        order = "003",
        runAlways = true
    )
    fun createUsersCollection(mongockTemplate: MongockTemplate) {
        logger.info("Create users collection")
        val userList = mutableListOf<User>()
        for (i in 1..5) {
            userList.add(
                User(
                    displayName = faker.name().name(),
                    email = faker.internet().emailAddress(),
                    name = faker.name().name(),
                    password = faker.internet().password(),
                    username = faker.name().username()
                )
            )
        }
        mongockTemplate.insertAll(userList)
    }
}
