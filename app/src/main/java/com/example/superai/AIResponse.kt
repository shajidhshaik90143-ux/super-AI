object AIResponse {

    fun getResponse(message: String): String {

        return when {

            message.contains("hello") ->
                "Hello Shajidh"

            message.contains("how are you") ->
                "I am fine"

            message.contains("your name") ->
                "I am Super AI"

            message.contains("who created you") ->
                "I was created by Shajidh"

            else ->
                "I am still learning. Please try another command."
        }
    }
}