import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const LIST: AppRouteRecordRaw = {
  path: '/ops',
  name: 'ops',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.ops',
    requiresAuth: true,
    icon: 'icon-list',
    order: 1,
  },
  children: [
    {
      path: 'ops-project',
      name: 'Project',
      component: () => import('@/views/ops/project/index.vue'),
      meta: {
        locale: 'menu.ops.project.list',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'ops-server',
      name: 'Server',
      component: () => import('@/views/ops/server/index.vue'),
      meta: {
        locale: 'menu.ops.server.list',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'ops-software',
      name: 'Software',
      component: () => import('@/views/ops/software/index.vue'),
      meta: {
        locale: 'menu.ops.software.list',
        requiresAuth: true,
        roles: ['*'],
      },
    },

    {
      path: 'ops-script',
      name: 'Script',
      component: () => import('@/views/ops/script/index.vue'),
      meta: {
        locale: 'menu.ops.script.list',
        requiresAuth: true,
        roles: ['*'],
      },
    },

    {
      path: 'ops-deploy',
      name: 'Deploy',
      component: () => import('@/views/ops/deploy/index.vue'),
      meta: {
        locale: 'menu.ops.deploy.list',
        requiresAuth: true,
        roles: ['*'],
      },
    },

    {
      path: 'ops-baseConfig',
      name: 'BaseConfig',
      component: () => import('@/views/ops/baseConfig/index.vue'),
      meta: {
        locale: 'menu.ops.baseConfig.list',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
};

export default LIST;
