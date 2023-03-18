import axios from 'axios';
import query from 'query-string';

const BASE_URL = '/admin/ops/software';


export interface SoftwareParam {
    softwareName?: string;
    page?: number;
    size?: number;
    sort?: Array<string>;
}

export interface SoftwareResponse {
    id?: number;
    softwareName: string;
    softwareFilename?: string;
    softwarePath?: string;
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

export interface SoftwareSaveCommand {
    id?: number;
    softwareName: string;
    softwareFilename?: string;
    softwarePath?: string;
    installPath?: string;
    envPath?: string;
    envContent?: string;
    installScriptPath?: string;
    status?: number;
}

export interface SoftwareResponseList {
    list: SoftwareResponse[];
    total: number;
}


// 通过SoftwareParam参数获取分页列表
export function pageSoftware(params: SoftwareParam) {
    return axios.get<SoftwareResponseList>(`${BASE_URL}/page`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    })
}

// 通过id获取项目信息
export function getSoftware(id: number) {
    return axios.get<SoftwareResponse>(`${BASE_URL}/${id}`);
    
}

export function listSoftware(params: SoftwareParam) {
    return axios.get<SoftwareResponse[]>(`${BASE_URL}/list`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    });
}


// 添加项目
export function addSoftware(params: SoftwareSaveCommand) {
    return axios.post(`${BASE_URL}`, params);
}

// 更新项目
export function updateSoftware(params: SoftwareSaveCommand) {
    return axios.put(`${BASE_URL}`, params);
}

// 批量删除项目
export function deleteSoftware(ids: number | Array<number>) {
    return axios.delete(`${BASE_URL}/${ids}`);
}

// 切换项目状态
export function switchSoftwareStatus(id: number) {
    return axios.patch(`${BASE_URL}/switch/status/${id}`);
}