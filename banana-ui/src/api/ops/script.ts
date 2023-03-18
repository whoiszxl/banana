import axios from 'axios';
import query from 'query-string';

const BASE_URL = '/admin/ops/script';


export interface ScriptParam {
    scriptName?: string;
    page?: number;
    size?: number;
    sort?: Array<string>;
}

export interface ScriptResponse {
    id?: number;
    scriptTitle: string;
    scriptName: string;
    scriptPath?: string;
    scriptContent?: string;
    description?: string;
    status?: number;
    createdBy?: string;
    updatedBy?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ScriptSaveCommand {
    id?: number;
    scriptTitle: string;
    scriptName: string;
    scriptPath?: string;
    scriptContent?: string;
    description?: string;
    status?: number;
}

export interface ScriptResponseList {
    list: ScriptResponse[];
    total: number;
}


// 通过ScriptParam参数获取分页列表
export function pageScript(params: ScriptParam) {
    return axios.get<ScriptResponseList>(`${BASE_URL}/page`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    })
}

// 通过id获取脚本信息
export function getScript(id: number) {
    return axios.get<ScriptResponse>(`${BASE_URL}/${id}`);
    
}

export function listScript(params: ScriptParam) {
    return axios.get<ScriptResponse[]>(`${BASE_URL}/list`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    });
}


// 添加脚本
export function addScript(params: ScriptSaveCommand) {
    return axios.post(`${BASE_URL}`, params);
}

// 更新脚本
export function updateScript(params: ScriptSaveCommand) {
    return axios.put(`${BASE_URL}`, params);
}

// 批量删除脚本
export function deleteScript(ids: number | Array<number>) {
    return axios.delete(`${BASE_URL}/${ids}`);
}

// 切换脚本状态
export function switchScriptStatus(id: number) {
    return axios.patch(`${BASE_URL}/switch/status/${id}`);
}