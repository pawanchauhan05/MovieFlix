package com.app.movieflix.utilities

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class UtilityTest {

    @Test
    fun getRating_shouldReturnFourRating() {
        val rating = Utility.getRating(8f, 10f)
        Assert.assertEquals(4f, rating)
    }
}