-- Create applications table
CREATE TABLE IF NOT EXISTS isl_applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    job_role VARCHAR(255),
    matched_skills VARCHAR(1024),
    missing_skills VARCHAR(1024),
    match_score INT,
    status VARCHAR(255),
    reason VARCHAR(1024),
    resume_path VARCHAR(255),
    created_at TIMESTAMP
      
);

-- Create jobs table (Updated)
CREATE TABLE IF NOT EXISTS isl_jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    role VARCHAR(255),
    location VARCHAR(255),
    description TEXT,
    salary_min INT,
    salary_max INT,
    type VARCHAR(100),
    level VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    applicants INT DEFAULT 0
);

-- Create opportunities table
CREATE TABLE IF NOT EXISTS isl_opportunities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company VARCHAR(255),
    role VARCHAR(255),
    location VARCHAR(255),
    description TEXT
);

-- Create users table
CREATE TABLE IF NOT EXISTS isl_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    role VARCHAR(100),
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

-- Updated candidates table
CREATE TABLE IF NOT EXISTS isl_candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    role VARCHAR(100),
    applied_role VARCHAR(100),
    department VARCHAR(100),
    employment_type VARCHAR(100),
    work_type VARCHAR(100),
    applied_date DATE,
    status VARCHAR(100),
    email VARCHAR(100),
    position VARCHAR(100),
    attachments LONGBLOB,
    score INT,
    phone VARCHAR(20),
    experience INT,
    job_id BIGINT,
    FOREIGN KEY (job_id) REFERENCES isl_jobs(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS isl_assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    question TEXT,
    options TEXT,                 -- store JSON string of options
    correct_answer TEXT,
    type VARCHAR(100),
    step_order INT,
    FOREIGN KEY (job_id) REFERENCES isl_jobs(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS isl_candidate_steps (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT UNSIGNED,
    assessment_id BIGINT,
    step_order INT,
    step_name VARCHAR(255),
    status VARCHAR(100),
    completed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (candidate_id) REFERENCES isl_candidates(id) ON DELETE CASCADE,
    FOREIGN KEY (assessment_id) REFERENCES isl_assessments(id) ON DELETE CASCADE
);

-- STEP 3: isl_reviews table
CREATE TABLE IF NOT EXISTS isl_reviews (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    step_id BIGINT UNSIGNED,
    reviewer VARCHAR(255),
    feedback TEXT,
    score INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (step_id) REFERENCES isl_candidate_steps(id) ON DELETE CASCADE
);

-- Table to store predefined assessment steps per role
DROP TABLE IF EXISTS isl_assessment_templates;

CREATE TABLE IF NOT EXISTS isl_assessment_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(255) NOT NULL,
    step_order INT,
    step_name VARCHAR(255)
);

-- Default steps for Product Manager
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('Product Manager', 1, 'Resume Screening'),
('Product Manager', 2, 'Product Thinking Round'),
('Product Manager', 3, 'Hiring Manager Round');

-- Default steps for Backend Developer
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('Backend Developer', 1, 'Resume Screening'),
('Backend Developer', 2, 'Data Structures Round'),
('Backend Developer', 3, 'System Design Round'),
('Backend Developer', 4, 'CTO Round');

-- Default steps for Frontend Developer
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('Frontend Developer', 1, 'Resume Screening'),
('Frontend Developer', 2, 'UI/UX Evaluation Round'),
('Frontend Developer', 3, 'Live Coding Round'),
('Frontend Developer', 4, 'CTO Round');

-- === Software Developer Steps ===
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('Software Developer', 1, 'Resume Screening'),
('Software Developer', 2, 'Online Coding Assessment'),
('Software Developer', 3, 'Technical Screening'),
('Software Developer', 4, 'Technical Interview Rounds'),
('Software Developer', 5, 'Behavioral & Culture Fit Interview'),
('Software Developer', 6, 'Final Round / Panel Interview'),
('Software Developer', 7, 'Offer & Negotiation');

-- === Regulatory Affairs Manager ===
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('Regulatory Affairs Manager', 1, 'Resume Screening / Application Review'),
('Regulatory Affairs Manager', 2, 'Initial HR Screening'),
('Regulatory Affairs Manager', 3, 'Technical Interview – Round 1'),
('Regulatory Affairs Manager', 4, 'Scenario-Based Assessment'),
('Regulatory Affairs Manager', 5, 'Managerial / Cross-Functional Interview'),
('Regulatory Affairs Manager', 6, 'Behavioral / HR Round'),
('Regulatory Affairs Manager', 7, 'Reference Check & Final Offer');

-- === Production Engineer ===
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('Production Engineer', 1, 'Application & Resume Screening'),
('Production Engineer', 2, 'Telephonic/Initial HR Screening'),
('Production Engineer', 3, 'Technical Interview'),
('Production Engineer', 4, 'Practical/Case-Based Assessment'),
('Production Engineer', 5, 'Managerial/Panel Interview'),
('Production Engineer', 6, 'HR Interview/Final Discussion'),
('Production Engineer', 7, 'Reference Check & Offer Rollout');

-- === CEO ===
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('CEO', 1, 'Pre-Interview Evaluation'),
('CEO', 2, 'Initial Screening'),
('CEO', 3, 'Executive Assessment'),
('CEO', 4, 'Board-Level Interviews'),
('CEO', 5, 'Stakeholder Engagement'),
('CEO', 6, 'Final Presentation / Case Study'),
('CEO', 7, 'Reference Checks'),
('CEO', 8, 'Negotiation & Appointment');

-- === Telecom Engineer ===
INSERT INTO isl_assessment_templates (role, step_order, step_name) VALUES
('Telecom Engineer', 1, 'Resume & Application Screening'),
('Telecom Engineer', 2, 'Telephonic / Virtual Screening'),
('Telecom Engineer', 3, 'Technical Assessment'),
('Telecom Engineer', 4, 'Technical Interview'),
('Telecom Engineer', 5, 'Behavioral / HR Interview'),
('Telecom Engineer', 6, 'Final Interview / Managerial Round'),
('Telecom Engineer', 7, 'Reference Check & Offer');
