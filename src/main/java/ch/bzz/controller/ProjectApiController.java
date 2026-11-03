package ch.bzz.controller;

import ch.bzz.data.Project;
import ch.bzz.generated.api.ProjectApi;
import ch.bzz.generated.model.LoginProject200Response;
import ch.bzz.generated.model.LoginRequest;
import ch.bzz.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Slf4j
@RestController
public class ProjectApiController implements ProjectApi {

    private final ProjectRepository projectRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public ProjectApiController(ProjectRepository projectRepository, JwtUtil jwtUtil) {
        this.projectRepository = projectRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<Void> createProject(LoginRequest loginRequest) {
        if (loginRequest == null
                || !StringUtils.hasText(loginRequest.getProjectName())
                || !StringUtils.hasText(loginRequest.getPassword())) {
            log.error("projectName and password are required");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "projectName and password are required");
        }

        String projectName = loginRequest.getProjectName().trim();
        if (projectRepository.existsById(projectName)) {
            log.error("Project already exists: {}", projectName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project already exists");
        }

        Project project = new Project(projectName, encoder.encode(loginRequest.getPassword()));
        projectRepository.save(project);
        log.info("Created project {}", projectName);

        return ResponseEntity.created(URI.create("/api/projects/" + projectName)).build();
    }

    @Override
    public ResponseEntity<LoginProject200Response> loginProject(LoginRequest loginRequest) {
        if (loginRequest == null
                || !StringUtils.hasText(loginRequest.getProjectName())
                || !StringUtils.hasText(loginRequest.getPassword())) {
            log.error("Invalid credentials: missing projectName or password");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String projectName = loginRequest.getProjectName().trim();
        Project project = projectRepository.findById(projectName).orElse(null);
        if (project == null || !encoder.matches(loginRequest.getPassword(), project.getPasswordHash())) {
            log.error("Invalid credentials for project {}", projectName);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        LoginProject200Response body = new LoginProject200Response();
        body.setAccessToken(jwtUtil.generateToken(projectName));
        log.info("Login successful for project {}", projectName);
        return ResponseEntity.ok(body);
    }
}
