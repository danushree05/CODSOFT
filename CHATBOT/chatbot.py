import re

# Define rules and responses
rules = {
    "hi|hello|hey": "Hello! How can I assist you today?",
    "how are you": "I'm just a bot, but I'm doing well. Thanks for asking!",
    "what is your name": "I'm a chatbot designed by OpenAI. You can call me ChatGPT!",
    "bye|goodbye": "Goodbye! Have a great day!",
    "what can you do": "I can provide information, answer questions, or just have a chat with you!",
    "tell me a joke": "Why don't scientists trust atoms? Because they make up everything!",
    "what is the time": "I'm sorry, I don't have access to real-time information.",
    ".+": "I'm sorry, I didn't understand that. Can you please rephrase?"
}

# Function to match user input with predefined rules
def chatbot_response(user_input):
    for pattern, response in rules.items():
        if re.match(f"^{pattern}$", user_input.lower()):
            return response
    return "I'm sorry, I didn't understand that. Can you please rephrase?"

# Main function to interact with the chatbot
def main():
    print("Welcome to the Chatbot! Type 'bye' to exit.")
    while True:
        user_input = input("You: ")
        if user_input.lower() == 'bye':
            print(chatbot_response(user_input))
            break
        print("Chatbot:", chatbot_response(user_input))

if __name__ == "__main__":
    main()
