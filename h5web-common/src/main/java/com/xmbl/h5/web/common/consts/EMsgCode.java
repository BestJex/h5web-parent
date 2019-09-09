package com.xmbl.h5.web.common.consts;

public enum EMsgCode {
	no_player_in_rank(10032),
	stage_id_can_not_be_null(30023),
	informal_environment_can_not_get_wechat_information(30024),
	query_stage_error(30026),
	stage_is_delete(30031),
	stage_is_off(30032),
	stage_tree_is_delete(30033),
	stage_tree_is_off(30034),
	share_failure(30035),
	
	player_id_can_not_be_null(80000),
	stage_not_exist(80001),
	error_query_rank(80002),
	rank_id_can_not_be_null(80003),
	error_query_rank_detail(80004),
	stage_rank_not_exist_or_stage_rank_id_is_wrong(80005),
	error_add_rank_by_no_param(80006),
	no_more_data(80007),
	game_rule_type_is_incorrectly(80008),
	use_step_is_null(80009),
	use_time_is_null(80010),
	remove_block_is_null(80011),
	score_is_null(80012),
	step_must_be_int(80013),
	time_must_be_int(80014),
	remove_block_num_must_be_int(80015),
	score_must_be_int(80016),
	commit_stage_error(80017),
	commit_stage_success(80018),
	player_auth_error(80019),
	error_query_player_info(80020),
	success(80021),
	wxuserid_can_not_be_null(80022),
	wxuserid_auth_failure(80023),
	wx_auth_success(80024),
	wx_auth_failure(80025),
	params_can_not_be_null(80026),
	stageId_must_be_int(80027),
	game_query_stage_error(80028),
	query_success(80029),
	
	error(90013),
	
	;
	private EMsgCode(int code) {
		this.code = code;
	}
	public int code;

}
