import axios from 'axios';
import query from 'query-string';

const BASE_URL = '/admin/ops/project';


export interface ProjectParam {
    name?: string;
    page?: number;
    size?: number;
    sort?: Array<string>;
}

export interface ProjectResponse {
    id: number;
    name: string;
    description?: string;
    markdown?: string;
    platformType?: number;
    platformParams?: string;
    createdBy?: string;
    updatedBy?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ProjectSaveCommand {
    id?: number;
    name: string;
    description?: string;
    markdown?: string;
    platformType?: number;
    platformParams?: string;
    createdBy?: string;
    updatedBy?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ProjectResponseList {
    list: ProjectResponse[];
    total: number;
}


// 通过ProjectParam参数获取分页列表
export function pageProject(params: ProjectParam) {
    return axios.get<ProjectResponseList>(`${BASE_URL}/page`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    })
}

// 通过id获取项目信息
export function getProject(id: number) {
    return axios.get<ProjectResponse>(`${BASE_URL}/${id}`);
}

// 添加项目
export function addProject(params: ProjectSaveCommand) {
    return axios.post(`${BASE_URL}/add`, params);
}

// 更新项目
export function updateProject(params: ProjectSaveCommand) {
    return axios.put(`${BASE_URL}`, params);
}

// 批量删除项目
export function deleteProject(ids: number | Array<number>) {
    return axios.delete(`${BASE_URL}/delete/${ids}`);
}

// 切换项目状态
export function switchProjectStatus(id: number) {
    return axios.patch(`${BASE_URL}/switch/status/${id}`);
}

export function generateProject(id: number) {
    return axios.post(`${BASE_URL}/generate/${id}`);
}

