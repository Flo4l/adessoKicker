package de.adesso.adessoKicker.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import de.adesso.adessoKicker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import de.adesso.adessoKicker.objects.Team;
import de.adesso.adessoKicker.repositories.UserRepository;
import de.adesso.adessoKicker.services.TeamService;

/**
 * Controller managing Teams
 * @author caylak
 *
 */
@RestController
public class TeamController {

    @Autowired
    TeamService teamService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    /**
     * gets all teams
     * @return
     */
    @GetMapping("/teams")
    public ModelAndView getAllTeams()
    {
        ModelAndView modelAndView = new ModelAndView();
        List<Team> allTeams = new ArrayList<>();
        teamService.getAllTeams().forEach(allTeams::add);
        modelAndView.addObject("teams", allTeams);
        return modelAndView;
    }

    /**
     * gets a single team specified by its id
     * @param id
     * @return
     */
    @GetMapping("/teams/{id}")
    public Team getOneTeam(@PathVariable long id)
    {

        return teamService.getOneTeam(id);
    }

    /**
     * ui for team creation
     * @return
     */
    @GetMapping("/teams/add")
    public ModelAndView showTeamCreation() {
        ModelAndView modelAndView = new ModelAndView();
        Team team = new Team();
        modelAndView.addObject("team", team);
        modelAndView.addObject("users", userService.getAllUsers());
        modelAndView.setViewName("teamadd");
        return modelAndView;
    }

    /**
     * POST chosen players and create a team with them and add the teamId to the players Team List
     * @param team
     * @param bindingResult
     * @return
     */
    @PostMapping("/teams/add")
    public ModelAndView createNewTeam(@Valid Team team, long playerAId, long playerBId, BindingResult bindingResult)
    {
        ModelAndView modelAndView = new ModelAndView();
        Team teamExists = teamService.findByTeamName(team.getTeamName());
        if (teamExists != null) {
            bindingResult.rejectValue("teamName", "error.teamName", "Fail: Team Name already exists.");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("teamadd");
        }
        else
        {
            team.setPlayerA(userService.getUserById(playerAId));
            team.setPlayerB(userService.getUserById(playerBId));
            teamService.addTeam(team);
            userService.addTeamIdToUser(team, playerAId);
            userService.addTeamIdToUser(team, playerBId);
            modelAndView.addObject("successMessage", "Success: Team has been added.");
            modelAndView.addObject("team", new Team());
            modelAndView.addObject("users", userService.getAllUsers());
            modelAndView.setViewName("teamadd");

        }

        return modelAndView;
    }

    /**
     * deletes team identified by its id
     * @param id
     */
    @RequestMapping(method=RequestMethod.DELETE, value="/teams/delete/{id}")
    public void deleteTeam(@PathVariable long id)
    {
        teamService.deleteTeam(id);
    }

    /**
     * updates team identified by the actual object and the id
     * @param team
     * @param id
     */
    @RequestMapping(method=RequestMethod.PUT, value="/teams/update/{id}")
    public void updateTeam(@RequestBody Team team, @PathVariable long id)
    {
        teamService.updateTeam(team, id);
    }
}
