# Cabinet360 AI Service

🤖 **Intelligent Medical Assistant Microservice** - AI-powered chatbot and medical literature integration for healthcare professionals.

## 🌟 Features

### 🧠 Medical Chatbot
- **AI-powered conversations** with medical context awareness
- **Conversation management** - create, view, delete conversations
- **Medical context integration** - incorporate patient/case information
- **Suggested follow-up questions** for enhanced clinical workflow
- **Token usage tracking** for cost management
- **Fallback mechanisms** for service reliability

### 📚 PubMed Integration
- **Real-time medical literature search** via PubMed E-utilities API
- **AI-generated article summaries** for quick insights
- **Search history tracking** for research continuity
- **Relevance scoring** to prioritize most relevant articles
- **Medical context-aware searches** with specialty filtering
- **Intelligent caching** for performance optimization

### 🔒 Security Features
- **JWT-based authentication** (validated from auth-service)
- **Role-based access control** (DOCTOR, ADMIN only)
- **Secure API communication** with other microservices
- **Rate limiting** to prevent abuse of AI APIs
- **Input sanitization** and validation

### 🎯 AI Provider Support
- **OpenAI GPT models** (GPT-3.5-turbo, GPT-4)
- **Free alternatives** (Hugging Face, Google Gemini)
- **Local AI models** (Ollama integration)
- **Fallback strategies** for high availability

## 🏗️ Architecture

~~~
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Auth Service  │    │   Core Service  │    │  Frontend Apps  │
│   (Port 8091)   │    │   (Port 8092)   │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
              ┌─────────────────────────────────┐
              │         AI Service              │
              │         (Port 8100)             │
              │                                 │
              │  ┌─────────────────────────┐    │
              │  │    Chatbot Engine       │    │
              │  │  - Conversation Mgmt    │    │
              │  │  - Context Awareness    │    │
              │  │  - Response Generation  │    │
              │  └─────────────────────────┘    │
              │                                 │
              │  ┌─────────────────────────┐    │
              │  │   PubMed Integration    │    │
              │  │  - Literature Search    │    │
              │  │  - Article Summaries    │    │
              │  │  - Research History     │    │
              │  └─────────────────────────┘    │
              └─────────────────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │  ai_service_db  │
                    └─────────────────┘
                                 │
               ┌─────────────────────────────────────┐
               │         External APIs               │
               │  ┌─────────────┐ ┌─────────────┐   │
               │  │  OpenAI     │ │   PubMed    │   │
               │  │   API       │ │   E-utils   │   │
               │  └─────────────┘ └─────────────┘   │
               └─────────────────────────────────────┘
~~~

## 🚀 Quick Start

### Prerequisites
- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL 15+**
- **OpenAI API Key** (or alternative AI provider)
- **Running Auth Service** (for JWT validation)

### 1. Environment Setup

~~~env
# Server Configuration
PORT=8100

# Database Configuration
DB_HOST=localhost
DB_PORT=5450
DB_NAME=ai_service_db
DB_USERNAME=user
DB_PASSWORD=pass

# JWT Configuration (must match auth-service)
JWT_SECRET=cabinet360supersecurekeymustbeatleast32chars!

# === AI PROVIDER SELECTION ===
# Choose: openai, huggingface, gemini, ollama
AI_PROVIDER=openai

# === OPENAI CONFIGURATION ===
OPENAI_API_KEY=your_openai_api_key_here
OPENAI_MODEL=gpt-3.5-turbo
OPENAI_MAX_TOKENS=1000

# === FREE ALTERNATIVES ===
GEMINI_API_KEY=your_gemini_api_key
GEMINI_MODEL=gemini-pro

AI_MODEL=microsoft/DialoGPT-medium
AI_MEDICAL_MODEL=dmis-lab/biobert-base-cased-v1.1

OLLAMA_URL=http://localhost:11434
OLLAMA_MODEL=medalpaca

# === PUBMED CONFIGURATION ===
PUBMED_API_KEY=your_pubmed_api_key
PUBMED_MAX_RESULTS=10
PUBMED_CACHE_TTL=24

# === EXTERNAL SERVICES ===
AUTH_SERVICE_URL=http://localhost:8091
CORE_SERVICE_URL=http://localhost:8092

# === PERFORMANCE SETTINGS ===
AI_TIMEOUT=30
AI_RETRY_MAX=3
AI_RETRY_DELAY=2
RATE_LIMIT_RPM=30
RATE_LIMIT_ENABLED=true

# === DEVELOPMENT ===
DEV_MODE=false
ENABLE_SWAGGER=true
~~~

### 2. Database Setup

~~~sql
-- Create database and user
CREATE DATABASE ai_service_db;
CREATE USER ai_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE ai_service_db TO ai_user;

-- Connect to database
\c ai_service_db;

-- Tables will be auto-created by JPA/Hibernate
~~~

### 3. Run the Service

~~~bash
# Development mode
mvn spring-boot:run

# With specific profile
mvn spring-boot:run -Dspring.profiles.active=dev

# Production build
mvn clean package
java -jar target/ai-service-0.0.1-SNAPSHOT.jar
~~~

### 4. Docker Deployment

~~~bash
# Build Docker image
docker build -t cabinet360/ai-service .

# Run with Docker Compose
docker-compose up -d ai-service

# Standalone run
docker run -p 8100:8100 --env-file .env cabinet360/ai-service
~~~

## 📚 API Documentation

### Base URL: `http://localhost:8100/api/v1/ai`

### 🔒 Authentication
All endpoints require valid JWT token:
~~~
Authorization: Bearer <jwt_token>
~~~

### 🤖 Chatbot Endpoints

#### Send Message to AI
~~~http
POST /chatbot/chat
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "message": "What are the latest treatments for diabetes?",
  "conversationId": 123,
  "medicalContext": "Patient: 45yo male with T2DM, HbA1c 8.5%"
}
~~~

**Response:**
~~~json
{
  "response": "...",
  "conversationId": 123,
  "messageId": 456,
  "suggestedQuestions": [...],
  "tokensUsed": 245,
  "processingTime": 1.2
}
~~~

#### Get Conversations List
~~~http
GET /chatbot/conversations?limit=20&offset=0
Authorization: Bearer <jwt_token>
~~~

**Response:**
~~~json
{
  "conversations": [...],
  "totalCount": 15,
  "hasMore": false
}
~~~

#### Get Conversation Messages
~~~http
GET /chatbot/conversations/{conversationId}/messages?limit=50
Authorization: Bearer <jwt_token>
~~~

#### Delete Conversation
~~~http
DELETE /chatbot/conversations/{conversationId}
Authorization: Bearer <jwt_token>
~~~

#### Get Usage Statistics
~~~http
GET /chatbot/usage/tokens?period=month
Authorization: Bearer <jwt_token>
~~~

### 📚 PubMed Endpoints

#### Search Medical Literature
~~~http
POST /pubmed/search
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "query": "diabetes treatment 2024",
  "maxResults": 10,
  ...
}
~~~

**Response:**
~~~json
{
  "searchId": "search_789",
  ...
}
~~~

#### Quick Literature Search
~~~http
GET /pubmed/quick-search?query=diabetes&maxResults=5
Authorization: Bearer <jwt_token>
~~~

#### Get Search History
~~~http
GET /pubmed/search/history?limit=20
Authorization: Bearer <jwt_token>
~~~

#### Get Article Details
~~~http
GET /pubmed/articles/{pmid}
Authorization: Bearer <jwt_token>
~~~

#### Generate Article Summary
~~~http
POST /pubmed/articles/{pmid}/summarize
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "focusArea": "treatment outcomes",
  "patientContext": "elderly patients with comorbidities",
  "summaryLength": "detailed"
}
~~~

## 🏥 Health & Monitoring

#### Health Check
~~~http
GET /health
~~~

**Response:**
~~~json
{
  "status": "UP",
  "service": "ai-service",
  ...
}
~~~

#### Service Information
~~~http
GET /info
~~~

**Response:**
~~~json
{
  "service": "Cabinet360 AI Service",
  "description": "...",
  ...
}
~~~

## 🗄️ Database Schema

### Core Tables

~~~sql
-- (voir détail complet fourni précédemment)
-- Chat Conversations, Messages, PubMed, Summaries, Usage, Provider Status, etc.
-- (cf. prompt complet pour toutes les tables et index)
~~~

### Performance Indexes

~~~sql
-- (voir détail complet fourni précédemment)
-- Toutes les indexes pour perf. et recherche rapide
~~~

## 🔧 Configuration

### Application Properties

~~~properties
# (voir détail complet fourni précédemment)
# Toutes les propriétés pour chaque provider, cache, logs, monitoring, etc.
~~~

## 🧪 Testing

### Unit & Integration Tests

~~~bash
# (voir détail complet fourni précédemment)
# Exemples pour tous les tests, coverage, Artillery load, etc.
~~~

## 🚀 Production Deployment

### Docker & Kubernetes

~~~dockerfile
# (voir détail complet fourni précédemment)
~~~

~~~yaml
# (voir détail complet fourni précédemment)
~~~

## 📊 Monitoring & Observability

### Metrics, Prometheus, Custom Metrics

~~~java
// (voir détail complet fourni précédemment)
~~~

~~~yaml
# prometheus.yml example
~~~

## 🚨 Troubleshooting

### Common Issues

~~~bash
# (voir détail complet fourni précédemment)
~~~

## 🔒 Security Best Practices

~~~java
// (voir détail complet fourni précédemment)
~~~

~~~properties
# (voir détail complet fourni précédemment)
~~~

## 🤝 Contributing

### Development Setup

~~~bash
# (voir détail complet fourni précédemment)
~~~

### Adding New AI Providers & Standards

- Follow the `AIProvider` interface and test with each config.
- Documentation and API tests required for all new features.

## 📋 Roadmap

### Current Version (v1.0)
- ✅ Multi-provider AI integration (OpenAI, Gemini, Ollama)
- ✅ Medical chatbot with context awareness
- ✅ PubMed integration with AI summaries
- ✅ Conversation management
- ✅ Token usage tracking

### Next Release (v1.1)
- 🔄 Voice-to-text integration
- 🔄 Medical image analysis
- 🔄 Clinical decision support
- 🔄 Multi-language support
- 🔄 Advanced caching strategies

### Future Releases (v2.0+)
- 📅 Custom medical AI model training
- 📅 Real-time collaboration features
- 📅 Integration with medical devices
- 📅 Predictive analytics
- 📅 Blockchain-based audit trails

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

### Documentation
- **API Reference**: Available at `/swagger-ui.html` when running
- **Postman Collection**: See `postman/` directory
- **Integration Guide**: See `docs/integration-guide.md`
- **AI Provider Setup**: See `docs/ai-providers.md`

### Getting Help
- **Issues**: Report bugs via GitHub Issues
- **Feature Requests**: Use GitHub Discussions
- **Email**: [nawfalrazouk7@gmail.com](mailto:nawfalrazouk7@gmail.com)

### AI Provider Support
- **OpenAI Issues**: [OpenAI status page](https://status.openai.com/)
- **Gemini Issues**: [Google AI status](https://status.cloud.google.com/)
- **Local Model Issues**: See Ollama documentation

### Performance Issues
- **Critical Issues**: [Email](mailto:nawfalrazouk7@gmail.com)
- **Response Time**: 1-2 hours for critical AI service issues

---

🤖 **Cabinet360 AI Service** – The Intelligent Heart of Healthcare Automation
