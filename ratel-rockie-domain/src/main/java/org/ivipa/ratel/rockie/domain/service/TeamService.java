package org.ivipa.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.rockie.common.model.Team;
import org.ivipa.ratel.rockie.common.model.TeamAdd;
import org.ivipa.ratel.rockie.common.model.TeamDelete;
import org.ivipa.ratel.rockie.common.model.TeamPage;
import org.ivipa.ratel.rockie.common.model.TeamQuery;
import org.ivipa.ratel.rockie.common.model.TeamUpdate;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.entity.TeamDo;
import org.ivipa.ratel.rockie.domain.mapper.TeamMapper;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TeamService extends ServiceImpl<TeamMapper, TeamDo> {

    private final static int MAX_PAGE_SIZE = 99999999;

    public Page<Team> getTeams(Auth auth, TeamPage teamPage) {
        Page<Team> page = new Page<>(teamPage.getPageNum(), teamPage.getPageSize());
        List<Team> result = baseMapper.getTeams(page, auth.getOnlineCustomer().getCustomerId(), teamPage.getTeamName());
        return page.setRecords(result);
    }

    public Page<Team> getTeams(Long customerId) {
        Page<TeamDo> page = new Page<>(1, MAX_PAGE_SIZE);
        QueryWrapper<TeamDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        queryWrapper.eq("deleted", 0);
        Page<TeamDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Team> teamPage = new Page<>(1, MAX_PAGE_SIZE);
        teamPage.setRecords(convertTeamDos(result.getRecords()));
        return teamPage;
    }

    public Team getTeam(Auth auth, TeamQuery teamQuery) {
        TeamDo teamDo = getById(teamQuery.getTeamId());
        if (teamDo == null || teamDo.getDeleted()) {
            throw RockieError.TEAM_TEAM_NOT_FOUND.newException();
        }
        if(!teamDo.getCustomerId().equals(auth.getOnlineCustomer().getCustomerId())) {
            throw RockieError.TEAM_TEAM_NOT_FOUND.newException();
        }
        Team team = convertTeamDo(teamDo);
        return team;
    }

    public Team addTeam(Auth auth, TeamAdd teamAdd) {
        Page<Team> teams;

        teams = getTeams(auth.getOnlineCustomer().getCustomerId());
        teams.getRecords().forEach(team -> {
            if (team.getTeamName().equalsIgnoreCase(teamAdd.getTeamName()) && !team.getDeleted()) {
                throw RockieError.TEAM_TEAM_NAME_EXISTS.newException();
            }
        });
        TeamDo teamDo = convertTeamAdd(teamAdd);
        teamDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
        teamDo.setTeamId(null);
        teamDo.setCreatedDate(LocalDateTime.now());
        teamDo.setUpdatedDate(LocalDateTime.now());
        save(teamDo);
        Team team = convertTeamDo(teamDo);
        return team;
    }

    public void updateTeam(Auth auth, TeamUpdate teamUpdate) {
        if (teamUpdate.getTeamId() == null) {
            throw RockieError.TEAM_TEAM_ID_IS_NULL.newException();
        }
        TeamDo oldTeamDo = getById(teamUpdate.getTeamId());
        if (oldTeamDo == null || oldTeamDo.getDeleted()) {
            throw RockieError.TEAM_TEAM_NOT_FOUND.newException();
        }
        if (!auth.getOnlineCustomer().getCustomerId().equals(oldTeamDo.getCustomerId())) {
            throw RockieError.TEAM_CUSTOMER_IS_INVALID.newException();
        }
        Page<Team> teams;
        teams = getTeams(auth.getOnlineCustomer().getCustomerId());
        teams.getRecords().forEach(team -> {
            if (!team.getTeamId().equals(teamUpdate.getTeamId()) && team.getTeamName().equalsIgnoreCase(teamUpdate.getTeamName()) && !team.getDeleted()) {
                throw RockieError.TEAM_TEAM_NAME_EXISTS.newException();
            }
        });
        TeamDo teamDo = convertTeamUpdate(teamUpdate, oldTeamDo);
        teamDo.setUpdatedDate(LocalDateTime.now());
        updateById(teamDo);
    }


    public void deleteTeam(Auth auth, TeamDelete teamDelete) {
        if (teamDelete.getTeamId() == null) {
            throw RockieError.TEAM_TEAM_ID_IS_NULL.newException();
        }
        TeamDo oldTeamDo = getById(teamDelete.getTeamId());
        if (oldTeamDo == null || oldTeamDo.getDeleted()) {
            throw RockieError.TEAM_TEAM_NOT_FOUND.newException();
        }
        if (!auth.getOnlineCustomer().getCustomerId().equals(oldTeamDo.getCustomerId())) {
            throw RockieError.TEAM_CUSTOMER_IS_INVALID.newException();
        }
        TeamDo teamDo = oldTeamDo;
        teamDo.setDeleted(true);
        updateById(teamDo);
    }

    private List<Team> convertTeamDos(List<TeamDo> teamDos) {
        List<Team> teams = new ArrayList<>();
        teamDos.forEach(
                teamDo -> {
                    Team team = new Team();
                    BeanUtils.copyProperties(teamDo, team);
                    teams.add(team);
                }
        );
        return teams;
    }

    private Team convertTeamDo(TeamDo teamDo) {
        Team team = new Team();
        BeanUtils.copyProperties(teamDo, team);
        return team;
    }

    private TeamDo convertTeamUpdate(TeamUpdate teamUpdate, TeamDo oldTeamDo) {
        BeanUtils.copyProperties(teamUpdate, oldTeamDo);
        return oldTeamDo;
    }

    private TeamDo convertTeamAdd(TeamAdd teamAdd) {
        TeamDo teamDo = new TeamDo();
        BeanUtils.copyProperties(teamAdd, teamDo);
        return teamDo;
    }
}
