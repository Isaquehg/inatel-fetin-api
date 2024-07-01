import json
import uuid

# Generate dinamic variables
dynamic_variables = {
    "teamName": f"team-{uuid.uuid4()}",
    "advisor_email": f"advisor-{uuid.uuid4()}@example.com",
    "advisor_name": f"Advisor {uuid.uuid4()}",
    "member1_email": f"member1-{uuid.uuid4()}@example.com",
    "member1_name": f"Member1 {uuid.uuid4()}",
    "member2_email": f"member2-{uuid.uuid4()}@example.com",
    "member2_name": f"Member2 {uuid.uuid4()}",
    "member3_email": f"member3-{uuid.uuid4()}@example.com",
    "member3_name": f"Member3 {uuid.uuid4()}",
    "member4_email": f"member4-{uuid.uuid4()}@example.com",
    "member4_name": f"Member4 {uuid.uuid4()}",
    "newPassword": f"password-{uuid.uuid4()}",
    "newAdvisor_email": f"newadvisor-{uuid.uuid4()}@example.com",
    "newAdvisor_name": f"New Advisor {uuid.uuid4()}"
}

# Save variables in JSON file
with open('e2e-tests/dynamic_variables.json', 'w') as f:
    json.dump(dynamic_variables, f)
