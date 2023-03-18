import axios from 'axios';
import query from 'query-string';

const BASE_URL = '/admin/ops/server';


export interface ServerParam {
    serverName?: string;
    projectId?: number;
    page?: number;
    size?: number;
    sort?: Array<string>;
}

export interface ServerResponse {
    id?: number;
    adminId?: number;
    serverName: string;
    serverOuterIp?: string;
    serverInnerIp?: string;
    serverPort?: string;
    serverUsername?: string;
    serverPassword?: string;
    platformType?: number;
    status?: number;
    createdBy?: string;
    updatedBy?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ServerSaveCommand {
    id?: number;
    adminId?: number;
    serverName: string;
    serverOuterIp?: string;
    serverInnerIp?: string;
    serverPort?: string;
    serverUsername?: string;
    serverPassword?: string;
    platformType?: number;
    status?: number;
}

export interface ServerResponseList {
    list: ServerResponse[];
    total: number;
}


// 通过ServerParam参数获取分页列表
export function pageServer(params: ServerParam) {
    return axios.get<ServerResponseList>(`${BASE_URL}/page`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    })
}

// 通过id获取项目信息
export function getServer(id: number) {
    return axios.get<ServerResponse>(`${BASE_URL}/${id}`);
    
}

export function listServer(params: ServerParam) {
    return axios.get<ServerResponse[]>(`${BASE_URL}/list`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    });
}


// 添加项目
export function addServer(params: ServerSaveCommand) {
    return axios.post(`${BASE_URL}`, params);
}

// 更新项目
export function updateServer(params: ServerSaveCommand) {
    return axios.put(`${BASE_URL}`, params);
}

// 批量删除项目
export function deleteServer(ids: number | Array<number>) {
    return axios.delete(`${BASE_URL}/${ids}`);
}

// 切换项目状态
export function switchServerStatus(id: number) {
    return axios.patch(`${BASE_URL}/switch/status/${id}`);
}

export function generateServer(id: number) {
    return axios.post(`${BASE_URL}/generate/${id}`);
}

export function connectTest(server: ServerResponse) {
    return axios.post(`${BASE_URL}/connect/test`, server);
}
