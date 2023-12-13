# AwsDynamoDBSample
**Task 1: Create an Amazon DynamoDB table using the AWS CLI**
aws dynamodb create-table ^
  --table-name Notes ^
  --attribute-definitions AttributeName=UserId,AttributeType=S AttributeName=NoteId,AttributeType=N ^
  --key-schema AttributeName=UserId,KeyType=HASH AttributeName=NoteId,KeyType=RANGE ^
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 

wait until table created - 
  aws dynamodb wait table-exists --table-name Notes

check table status - 
  aws dynamodb describe-table --table-name Notes | findstr TableStatus

**Task 2: Load data into the table**
mvn -q exec:java -Dexec.mainClass="dev.labs.dynamodb.notesLoadData"

**Task 3: Query data using a primary key and projections**
mvn -q exec:java -Dexec.mainClass="dev.labs.dynamodb.notesQuery"

**Task 4: Scan the table with a paginator**
mvn -q exec:java -Dexec.mainClass="dev.labs.dynamodb.notesScan"

**Task 5: Update an item in the Table**
mvn -q exec:java -Dexec.mainClass="dev.labs.dynamodb.notesUpdate"

**Task 6: Using the DynamoDBMapper class for CRUD operations**
mvn -q exec:java -Dexec.mainClass="dev.labs.dynamodb.notesCRUDmapper"


