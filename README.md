# Cabinet360 AI Service

🤖 AI-powered medical assistance microservice providing chatbot functionality and PubMed integration for healthcare professionals.

## Features

### 🧠 Medical Chatbot
- **AI-powered conversations** with medical context awareness
- **Conversation management** - create, view, delete conversations
- **Medical context integration** - incorporate patient/case information
- **Suggested follow-up questions** for enhanced clinical workflow
- **Token usage tracking** for cost management

### 📚 PubMed Integration
- **Real-time medical literature search** via PubMed API
- **AI-generated article summaries** for quick insights
- **Search history tracking** for research continuity
- **Relevance scoring** to prioritize most relevant articles
- **Medical context-aware searches**

### 🔒 Security Features
- **JWT-based authentication** (validated from auth-service)
- **Role-based access control** (DOCTOR, ADMIN only)
- **Secure API communication** with other microservices

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Auth Service  │    │   Core Service  │    │   AI Service    │
│   (Port 8080)   │    │   (Port 8081)   │    │   (Port 8082)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   (Per Service) │
                    └─────────────────┘
```

## Quick Start

### Prerequisites
- ✅ Java 17+
- ✅ Maven 3.8+
- ✅ PostgreSQL 15+
- ✅ OpenAI API Key
- ✅ Running Auth Service (for JWT validation)

### 1. Environment Setup

Create `.env` file:
```env
# Database
DB_HOST=localhost
DB_PORT=5434
DB_NAME=ai_service_db
DB_USERNAME=user
DB_PASSWORD=pass

# OpenAI Configuration
OPENAI_API_KEY=your_openai_api_key_here
OPENAI_MODEL=gpt-3.5-turbo
OPENAI_MAX_TOKENS=1000

# JWT Secret (must match auth-service)
JWT_SECRET=cabinet360supersecurekeymustbeatleast32chars!

# Development Mode
DEV_MODE=true
```

### 2. Database Setup

```sql
CREATE DATABASE ai_service_db;
CREATE USER user WITH PASSWORD 'pass';
GRANT ALL PRIVILEGES ON DATABASE ai_service_db TO user;
```

### 3. Run the Service

```bash
# Development
mvn spring-boot:run

# Production
mvn clean package
java -jar target/ai-service-0.0.1-SNAPSHOT.jar
```

### 4. Docker Deployment

```bash
# Build and run with Docker Compose
docker-compose up -d

# Or build Docker image
docker build -t cabinet360/ai-service .
docker run -p 8082:8082 --env-file .env cabinet360/ai-service
```

## API Documentation

### Authentication
All endpoints require valid JWT token in Authorization header:
```
Authorization: Bearer <jwt_token>
```

### Chatbot Endpoints

#### Send Message
```http
POST /api/v1/ai/chatbot/chat
Content-Type: application/json

{
  "message": "What are the latest treatments for diabetes?",
  "conversationId": 123,  // optional, creates new if null
  "medicalContext": "Patient: 45yo male with T2DM"  // optional
}
```

#### Get Conversations
```http
GET /api/v1/ai/chatbot/conversations
```

#### Get Conversation Messages
```http
GET /api/v1/ai/chatbot/conversations/{conversationId}/messages
```

#### Delete Conversation
```http
DELETE /api/v1/ai/chatbot/conversations/{conversationId}
```

### PubMed Endpoints

#### Search Articles
```http
POST /api/v1/ai/pubmed/search
Content-Type: application/json

{
  "query": "diabetes treatment 2024",
  "maxResults": 10,
  "patientContext": "45yo male with T2DM",
  "specialty": "endocrinology"
}
```

#### Quick Search
```http
GET /api/v1/ai/pubmed/quick-search?query=diabetes&maxResults=5
```

#### Search History
```http
GET /api/v1/ai/pubmed/search/history?limit=20
```

### Health Check
```http
GET /api/v1/ai/health
```

## Database Schema

### Core Tables

```sql
-- Chat Conversations
CREATE TABLE chat_conversations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    title VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT true
);

-- Chat Messages
CREATE TABLE chat_messages (
    id BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT NOT NULL REFERENCES chat_conversations(id),
    role VARCHAR(20) NOT NULL, -- 'USER' or 'ASSISTANT'
    content TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
    token_count INTEGER,
    medical_context TEXT
);

-- PubMed Searches
CREATE TABLE pubmed_searches (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    search_query VARCHAR(500) NOT NULL,
    search_terms VARCHAR(500) NOT NULL,
    search_date TIMESTAMP NOT NULL DEFAULT NOW(),
    results_count INTEGER,
    search_context TEXT
);

-- PubMed Articles (cached)
CREATE TABLE pubmed_articles (
    id BIGSERIAL PRIMARY KEY,
    pmid VARCHAR(20) NOT NULL UNIQUE,
    title TEXT NOT NULL,
    abstract_text TEXT,
    authors TEXT,
    journal VARCHAR(300),
    publication_date VARCHAR(50),
    keywords TEXT,
    doi VARCHAR(100),
    indexed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    ai_summary TEXT,
    relevance_score DOUBLE PRECISION
);
```

## Configuration

### OpenAI Settings
- **Model**: Configurable (default: gpt-3.5-turbo)
- **Max Tokens**: Adjustable per request type
- **Temperature**: Optimized for medical accuracy (0.7 for chat, 0.5 for summaries)

### PubMed Settings
- **API Key**: Optional but recommended for higher rate limits
- **Max Results**: Default 10, configurable per request
- **Caching**: Articles are cached to reduce API calls

### Security Settings
- **JWT Validation**: Shares secret with auth-service
- **Role Restrictions**: Only DOCTOR and ADMIN roles can access AI features
- **Rate Limiting**: Can be configured per user/endpoint

## Development

### Project Structure
```
src/main/java/com/cabinet360/ai/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/             # Data Transfer Objects
│   ├── request/     # Request DTOs
│   └── response/    # Response DTOs
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── repository/      # Data repositories
├── security/        # Security configuration
├── service/         # Business logic
└── util/           # Utility classes
```

### Key Services

#### `OpenAiChatService`
- Handles OpenAI API communication
- Manages conversation context and prompts
- Provides medical-specific system prompts

#### `ChatbotService`
- Manages conversation flow
- Persists chat history
- Generates conversation titles and suggestions

#### `PubMedService`
- Integrates with PubMed E-utilities API
- Parses XML responses and extracts article data
- Manages article caching and AI summaries

### Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn test -Dtest=**/*IntegrationTest

# Test with specific profile
mvn test -Dspring.profiles.active=test
```

## Production Deployment

### Docker Compose (Recommended)
```yaml
version: '3.8'
services:
  ai-service:
    image: cabinet360/ai-service:latest
    ports:
      - "8082:8082"
    environment:
      - DB_HOST=ai-db
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - JWT_SECRET=${JWT_SECRET}
    depends_on:
      - ai-db
    restart: unless-stopped

  ai-db:
    image: postgres:15
    environment:
      - POSTGRES_DB=ai_service_db
      - POSTGRES_USER=${DB_USERNAME}
      - POSTGRES_PASSWORD=${DB_PASSWORD}
    volumes:
      - ai_db_data:/var/lib/postgresql/data
    restart: unless-stopped

volumes:
  ai_db_data:
```

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ai-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: ai-service
  template:
    metadata:
      labels:
        app: ai-service
    spec:
      containers:
      - name: ai-service
        image: cabinet360/ai-service:latest
        ports:
        - containerPort: 8082
        env:
        - name: DB_HOST
          value: "postgres-service"
        - name: OPENAI_API_KEY
          valueFrom:
            secretKeyRef:
              name: ai-secrets
              key: openai-api-key
```

## Monitoring & Logging

### Health Checks
- **Endpoint**: `GET /api/v1/ai/health`
- **Database**: Automatic connection health check
- **External APIs**: OpenAI and PubMed connectivity status

### Metrics
- Token usage per user
- API response times
- Error rates by endpoint
- Cache hit rates for PubMed articles

### Logging Configuration
```properties
# application.properties
logging.level.com.cabinet360.ai=DEBUG
logging.level.org.springframework.web.reactive.function.client=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

## Troubleshooting

### Common Issues

#### OpenAI API Errors
```bash
# Check API key
curl -H "Authorization: Bearer $OPENAI_API_KEY" \
     https://api.openai.com/v1/models

# Verify token usage
GET /api/v1/ai/chatbot/usage/tokens
```

#### PubMed Connection Issues
```bash
# Test PubMed API directly
curl "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=diabetes&retmode=json"
```

#### Database Connection
```bash
# Check PostgreSQL connection
psql -h localhost -p 5434 -U user -d ai_service_db -c "\dt"
```

### Performance Optimization

1. **Database Indexing**
   ```sql
   CREATE INDEX idx_conversations_user_id ON chat_conversations(user_id);
   CREATE INDEX idx_messages_conversation_id ON chat_messages(conversation_id);
   CREATE INDEX idx_articles_pmid ON pubmed_articles(pmid);
   ```

2. **Caching Strategy**
    - PubMed articles cached indefinitely
    - AI summaries cached with articles
    - Search results cached for 1 hour

3. **Rate Limiting**
    - Implement per-user rate limits
    - Cache frequent PubMed queries
    - Batch OpenAI requests when possible

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## License

This project is part of the Cabinet360 medical management system.

---

🏥 **Cabinet360 AI Service** - Empowering healthcare with AI assistance