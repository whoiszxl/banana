import axios from 'axios';
import query from 'query-string';

const BASE_URL = '/admin/ops/base-config';


export interface BaseConfigParam {
    configKey?: string;
    page?: number;
    size?: number;
    sort?: Array<string>;
}

export interface BaseConfigResponse {
    id?: number;
    configKey: string;
    configValue?: string;
    status?: number;
    createdBy?: string;
    updatedBy?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface BaseConfigSaveCommand {
    id?: number;
    configKey: string;
    configValue?: string;
    status?: number;
}

export interface BaseConfigResponseList {
    list: BaseConfigResponse[];
    total: number;
}


// 通过BaseConfigParam参数获取分页列表
export function pageBaseConfig(params: BaseConfigParam) {
    return axios.get<BaseConfigResponseList>(`${BASE_URL}/page`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    })
}

// 通过id获取基础配置信息
export function getBaseConfig(id: number) {
    return axios.get<BaseConfigResponse>(`${BASE_URL}/${id}`);
    
}

export function listBaseConfig(params: BaseConfigParam) {
    return axios.get<BaseConfigResponse[]>(`${BASE_URL}/list`, {
        params, paramsSerializer: (obj) => {
            return query.stringify(obj);
        }
    });
}


// 添加基础配置
export function addBaseConfig(params: BaseConfigSaveCommand) {
    return axios.post(`${BASE_URL}`, params);
}

// 更新基础配置
export function updateBaseConfig(params: BaseConfigSaveCommand) {
    return axios.put(`${BASE_URL}`, params);
}

// 批量删除基础配置
export function deleteBaseConfig(ids: number | Array<number>) {
    return axios.delete(`${BASE_URL}/${ids}`);
}

// 切换基础配置状态
export function switchBaseConfigStatus(id: number) {
    return axios.patch(`${BASE_URL}/switch/status/${id}`);
}