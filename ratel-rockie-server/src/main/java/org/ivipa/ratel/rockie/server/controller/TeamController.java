package org.ivipa.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.Team;
import org.ivipa.ratel.rockie.common.model.TeamAdd;
import org.ivipa.ratel.rockie.common.model.TeamDelete;
import org.ivipa.ratel.rockie.common.model.TeamPage;
import org.ivipa.ratel.rockie.common.model.TeamQuery;
import org.ivipa.ratel.rockie.common.model.TeamUpdate;
import org.ivipa.ratel.rockie.domain.service.TeamService;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.controller.GenericController;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("team")
public class TeamController extends GenericController {

    @Autowired
    private TeamService teamService;

    @PostMapping("teams")
    @Audit
    public Result<Page<Team>> getTeams(Auth auth, @RequestBody TeamPage teamPage) {
        Page<Team> customers = teamService.getTeams(auth, teamPage);
        return Result.success(customers);
    }

    @PostMapping("team")
    @Audit
    public Result<Team> getTeam(Auth auth, @RequestBody TeamQuery teamQuery) {
        Team team = teamService.getTeam(auth, teamQuery);
        return Result.success(team);
    }

    @PostMapping("add")
    @Audit
    public Result<Team> addTeam(Auth auth, @RequestBody TeamAdd teamAdd) {
        Team team = teamService.addTeam(auth, teamAdd);
        return Result.success(team);
    }

    @PostMapping("update")
    @Audit
    public Result updateTeam(Auth auth, @RequestBody TeamUpdate teamUpdate) {
        teamService.updateTeam(auth, teamUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteTeams(Auth auth, @RequestBody TeamDelete teamDelete) {
        teamService.deleteTeam(auth, teamDelete);
        return Result.success();
    }

}
