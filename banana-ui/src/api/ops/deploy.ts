import axios from 'axios';
import query from 'query-string';

const BASE_URL = '/admin/ops/deploy';


export interface DeployParam {
    deployName?: string;
    page?: number;
    size?: number;
    sort?: Array<string>;
}

export interface DeployResponse {
    id?: number;
    adminId?: number;
    deployName: string;
    description?: string;
    softwareId?: number;
    serverIds?: string;
    deployLogs?: string;
    initStatus?: number;
    status?: number;
    createdBy?: string;
    updatedBy?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface DeploySaveCommand {
    id?: number;
    adminId?: number;
    deployName: string;
    description?: string;
    softwareId?: number;
    serverIds?: string;
    deployLogs?: string;
    initStatus?: number;
    status?: number;
}

export interface DeployResponseList {
    list: DeployResponse[];
    total: number;
}


// 通过DeployParam参数获取分页列表
export function pageDeploy(params: DeployParam) {
    return axios.get<DeployResponseList>(`${BASE_URL}/page`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    })
}

// 通过id获取脚本信息
export function getDeploy(id: number) {
    return axios.get<DeployResponse>(`${BASE_URL}/${id}`);
    
}

export function listDeploy(params: DeployParam) {
    return axios.get<DeployResponse[]>(`${BASE_URL}/list`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    });
}


// 添加脚本
export function addDeploy(params: DeploySaveCommand) {
    return axios.post(`${BASE_URL}`, params);
}

// 更新脚本
export function updateDeploy(params: DeploySaveCommand) {
    return axios.put(`${BASE_URL}`, params);
}

// 批量删除脚本
export function deleteDeploy(ids: number | Array<number>) {
    return axios.delete(`${BASE_URL}/${ids}`);
}

// 切换脚本状态
export function switchDeployStatus(id: number) {
    return axios.patch(`${BASE_URL}/switch/status/${id}`);
}

// 一键部署
export function deployDeploy(id: number) {
    return axios.post(`${BASE_URL}/deploy/${id}`);
}

// 初始化集群
export function initDeploy(id: number) {
    return axios.post(`${BASE_URL}/init/${id}`);
}

// 查看集群状态
export function statusDeploy(id: number) {
    return axios.post(`${BASE_URL}/status/${id}`);
}

// 启动集群
export function startDeploy(id: number) {
    return axios.post(`${BASE_URL}/start/${id}`);
}

// 启动集群
export function syncSoftware(id: number) {
    return axios.post(`${BASE_URL}/sync/software/${id}`);
}

