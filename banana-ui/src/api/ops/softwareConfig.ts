import axios from 'axios';
import query from 'query-string';

const BASE_URL = '/admin/ops/software-config';


export interface SoftwareConfigParam {
    softwareConfigName?: string;
    page?: number;
    size?: number;
    sort?: Array<string>;
}

export interface SoftwareConfigResponse {
    id?: number;
    softwareConfigName: string;
    softwareConfigFilename?: string;
    softwareConfigPath?: string;
    installPath?: string;
    envPath?: string;
    envContent?: string;
    installScriptPath?: string;
    status?: number;
    createdBy?: string;
    updatedBy?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface SoftwareConfigSaveCommand {
    id?: number;
    softwareConfigName: string;
    softwareConfigFilename?: string;
    softwareConfigPath?: string;
    installPath?: string;
    envPath?: string;
    envContent?: string;
    installScriptPath?: string;
    status?: number;
}

export interface SoftwareConfigResponseList {
    list: SoftwareConfigResponse[];
    total: number;
}


// 通过SoftwareConfigParam参数获取分页列表
export function pageSoftwareConfig(params: SoftwareConfigParam) {
    return axios.get<SoftwareConfigResponseList>(`${BASE_URL}/page`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    })
}

// 通过id获取项目信息
export function getSoftwareConfig(id: number) {
    return axios.get<SoftwareConfigResponse>(`${BASE_URL}/${id}`);
    
}

export function listSoftwareConfig(params: SoftwareConfigParam) {
    return axios.get<SoftwareConfigResponse[]>(`${BASE_URL}/list`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    });
}


// 添加项目
export function addSoftwareConfig(params: SoftwareConfigSaveCommand) {
    return axios.post(`${BASE_URL}`, params);
}

// 更新项目
export function updateSoftwareConfig(params: SoftwareConfigSaveCommand) {
    return axios.put(`${BASE_URL}`, params);
}

// 批量删除项目
export function deleteSoftwareConfig(ids: number | Array<number>) {
    return axios.delete(`${BASE_URL}/${ids}`);
}

// 切换项目状态
export function switchSoftwareConfigStatus(id: number) {
    return axios.patch(`${BASE_URL}/switch/status/${id}`);
}