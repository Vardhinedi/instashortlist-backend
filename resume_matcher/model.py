from sentence_transformers import SentenceTransformer, util

# Load pre-trained SBERT model
model = SentenceTransformer('all-MiniLM-L6-v2')

def get_resume_match_score(resume_text: str, job_description: str) -> dict:
    try:
        # Encode inputs
        resume_embedding = model.encode(resume_text, convert_to_tensor=True)
        jd_embedding = model.encode(job_description, convert_to_tensor=True)

        # Cosine similarity
        score = util.cos_sim(resume_embedding, jd_embedding).item()
        score = max(score, 0.0)  # Clamp negative scores to 0

        match_score = round(score * 100, 2)  # Convert to 0-100 scale

        return {
            "match_score": match_score,
            "justification": f"Semantic similarity between resume and JD is {match_score}%"
        }
    except Exception as e:
        return {
            "match_score": 0,
            "justification": f"Error during matching: {str(e)}"
        }
