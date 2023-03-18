<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', 'menu.ops.deploy.list']" />
    <a-card class="general-card" :title="$t('menu.ops.deploy.list')">

      <!-- 头部区域 -->
      <div class="header">
        <!-- 搜索栏 -->
        <div v-if="showQuery" class="header-query">
          <a-form ref="queryRef" :model="queryParams" layout="inline">
            <a-form-item field="name" hide-label>
              <a-input
                  v-model="queryParams.deployName"
                  placeholder="输入部署名或描述进行搜索"
                  allow-clear
                  style="width: 200px"
                  @press-enter="handleQuery"
              />
            </a-form-item>

            <a-form-item hide-label>
              <a-space>
                <a-button type="primary" @click="handleQuery">
                  <template #icon><icon-search /></template>查询
                </a-button>
                <a-button @click="resetQuery">
                  <template #icon><icon-refresh /></template>重置
                </a-button>
              </a-space>
            </a-form-item>
          </a-form>
        </div>
        <!-- 操作栏 -->
        <div class="header-operation">
          <a-row>
            <a-col :span="16">
              <a-space>
                <a-button v-permission="['ops:deploy:add']" type="primary" @click="toAdd">
                  <template #icon><icon-plus/></template>新增
                </a-button>

                <a-button
                    v-permission="['ops:deploy:update']"
                    type="primary"
                    status="success"
                    :disabled="single"
                    :title="single ? '请选择一条要修改的数据' : ''"
                    @click="toUpdate(checkedIds[0])"
                >
                  <template #icon><icon-edit /></template>修改
                </a-button>
                <a-button
                    v-permission="['ops:deploy:delete']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要删除的数据' : ''"
                    @click="handleBatchDelete"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
                <a-button
                    v-permission="['ops:deploy:export']"
                    :loading="exportLoading"
                    type="primary"
                    status="warning"
                    @click="handleExport"
                >
                  <template #icon><icon-download /></template>导出
                </a-button>

                <a-button
                    v-permission="['ops:deploy:init']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要初始化的集群' : ''"
                    @click="handleInit"
                >
                  <template #icon><icon-delete /></template>集群初始化
                </a-button>


                <a-button
                    v-permission="['ops:deploy:init']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要组件同步的集群' : ''"
                    @click="handleSyncSoftware"
                >
                  <template #icon><icon-delete /></template>组件同步
                </a-button>


              </a-space>
            </a-col>
            <a-col :span="8">
              <right-toolbar v-model:show-query="showQuery" @refresh="fetchData"/>
            </a-col>
          </a-row>
        </div>
      </div>


      <a-table
          row-key="id"
          :data="deployList"
          :loading="loading"
          :pagination="{
              showTotal: true,
              showPageSize: true,
              total: total,
              current: queryParams.page,
            }"
          :bordered="false"
          :size="size"
          @page-change="handlePageChange"
          @page-size-change="handlePageSizeChange"
          @selection-change="handleSelectionChange"
          :row-selection="{
              type: 'checkbox',
              showCheckedAll: true,
              onlyCurrent: false,
            }"
      >

        <template #columns>
          <a-table-column title="ID" data-index="id" :width="40" />
          <a-table-column title="部署名" :width="120">
            <template #cell="{ record }">
              <a-link @click="toDetail(record.id)">{{
                  record.deployName
                }}</a-link>
            </template>
          </a-table-column>

          <a-table-column title="部署描述" data-index="description" :width="200" />
          <a-table-column title="部署组件ID" data-index="softwareId" :width="100" />
          <a-table-column title="部署服务实例ID集合" data-index="serverIds" :width="150" />


          <a-table-column title="初始化状态" :width="90">
            <template #cell="{ record }">
              <span v-if="record.status === 0">未初始化</span>
              <span v-else-if="record.status === 1">已初始化</span>
              <span v-else>未知</span>
            </template>
          </a-table-column>


          <a-table-column title="状态" :width="70">
            <template #cell="{ record }">
              <span v-if="record.status === 1">已创建</span>
              <span v-else-if="record.status === 2">部署成功</span>
              <span v-else-if="record.status === 3">部署失败</span>
              <span v-else-if="record.status === 4">正在部署中</span>
              <span v-else>未知</span>
            </template>
          </a-table-column>

          <a-table-column title="创建时间" :width="130">
            <template #cell="{ record }">
              {{ record.createdAt }}
            </template>
          </a-table-column>

          <a-table-column title="操作" align="center" fixed="right" :width="200">

            <template #cell="{ record }">


              <a-button
                  v-permission="['ops:deploy:update']"
                  type="text"
                  size="small"
                  title="修改"
                  @click="toUpdate(record.id)"
              >
                <template #icon><icon-edit /></template> 
              </a-button>

              <a-popconfirm
                  content="确定删除吗？"
                  type="warning"
                  @ok="handleDelete([record.id])"
              >
                <a-button
                    v-permission="['ops:deploy:delete']"
                    type="text"
                    size="small"
                    title="删除"
                    :disabled="record.disabled"
                >
                  <template #icon><icon-delete /></template> 
                </a-button>
              </a-popconfirm>

              <a-popconfirm content="确定部署吗？" type="warning" @ok="handleDeploy(record.id)" >
                <a-button v-permission="['ops:deploy:deploy']" type="text" size="small" title="一键部署项目到服务器集群" :disabled="record.disabled">
                  <template #icon><icon-cloud /></template>一键部署
                </a-button>
              </a-popconfirm>

              <a-popconfirm content="确定启动吗？" type="warning" @ok="handleStartDeploy(record.id)" >
                <a-button v-permission="['ops:deploy:deploy']" type="text" size="small" title="集群一键启动" :disabled="record.disabled">
                  <template #icon><icon-cloud /></template>一键启动
                </a-button>
              </a-popconfirm>

              <a-button v-permission="['ops:deploy:deploy']" type="text" size="small" title="查询集群" @click="handleStatusDeploy(record.id)" :disabled="record.disabled">
                  <template #icon><icon-cloud /></template>状态查询
                </a-button>

            </template>

          </a-table-column>
        </template>


      </a-table>
    </a-card>




    <!-- 表单区域 -->
    <a-drawer :title="title" :visible="visible" :width="690" :mask-closable="false" unmount-on-close
        render-to-body @ok="handleOk" @cancel="handleCancel"
    >
    <a-form ref="formRef" :model="deploySaveForm" :rules="rules" :label-col-style="{ width: '94px' }"
          size="large" layout="inline" auto-label-width >

        <a-form-item label="部署名" field="deployName">
          <a-input
              v-model="deploySaveForm.deployName"
              placeholder="请输入部署名"
              style="width: 450px"
          />
        </a-form-item>

        <a-form-item label="描述" field="description">
          <a-input
              v-model="deploySaveForm.description"
              placeholder="请输入部署描述"
              style="width: 450px"
          />
        </a-form-item>

        <a-form-item label="部署组件ID" field="softwareId">

          <a-select v-model="deploySaveForm.softwareId" :style="{width:'450px'}" placeholder="请选择一个组件" >
            <a-option v-for="item,index of selectData" :value="item.id" v-bind:key="index" :label="item.softwareName"></a-option>
          </a-select>

        </a-form-item>

        <a-form-item label="实例ID" field="selectedOptions">
          <a-select
              v-model="selectedOptions"
              :options="serverOptions"
              placeholder="请选择需要部署的实例"
              :loading="serverIdsLoading"
              multiple
              allow-clear
              :allow-search="{ retainInputValue: true }"
              style="width: 431px"
          />
        </a-form-item>

      </a-form>
    </a-drawer>


    <a-drawer
        title="部署详情"
        :visible="detailVisible"
        :width="700"
        :footer="false"
        unmount-on-close
        render-to-body
        @cancel="handleDetailCancel"
    >
      <a-descriptions :column="2" bordered size="large">

        <a-descriptions-item label="部署名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.deployName }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="描述">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.description }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="组件ID">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.softwareId }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="部署服务实例ID集合">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.serverIds }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="状态">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>
              <a-tag v-if="deploy.status === 1">已创建</a-tag>
              <a-tag v-if="deploy.status === 2">部署成功</a-tag>
              <a-tag v-if="deploy.status === 3">部署失败</a-tag>
              <a-tag v-if="deploy.status === 4">正在部署中</a-tag>
            </span>
        </a-descriptions-item>

        <a-descriptions-item label="创建时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.createdAt }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.createdBy }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="更新者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.updatedBy }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="更新时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ deploy.updatedAt }}</span>
        </a-descriptions-item>

      </a-descriptions>
    </a-drawer>

  </div>



</template>

<script lang="ts" setup>
  import {getCurrentInstance, reactive, ref, toRefs} from 'vue';
  import useLoading from '@/hooks/loading';
  import {
    addDeploy,
    getDeploy,
    updateDeploy,
    deleteDeploy,
    DeploySaveCommand,
    DeployParam,
    DeployResponse,
    pageDeploy,
    deployDeploy,
    initDeploy,
    startDeploy,
    statusDeploy,
    syncSoftware
  } from "@/api/ops/deploy";
  import {DictResponseState} from "@/store/modules/dict/types";
  import '@wangeditor/editor/dist/css/style.css' // 引入 css

  import { onBeforeUnmount, shallowRef } from 'vue'
  import { Boot } from '@wangeditor/editor'
  import markdownModule from '@wangeditor/plugin-md'
  import { listSoftware, SoftwareResponse } from '@/api/ops/software';
  import { listServer } from '@/api/ops/server';
  import { Notification } from '@arco-design/web-vue';

  const showQuery = ref(true);
  Boot.registerModule(markdownModule)




  type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  const { proxy } = getCurrentInstance() as any;
  const { loading, setLoading } = useLoading(true);

  const size = ref<SizeProps>('medium');
  const checkedIds = ref<Array<number>>([]);
  const title = ref('');
  const single = ref(true);
  const multiple = ref(true);
  const selectData = ref<SoftwareResponse[]>();

  const data = reactive({
    queryParams: {
      deployName: undefined,
      page: 1,
      size: 10,
      sort: ['createdAt,desc'],
    },
    deploySaveForm: {} as DeploySaveCommand,
    rules: {
      deployName: [{ required: true, message: '请输入部署名' }],
      deployOuterIp: [{ required: true, message: '请输入部署外网IP' }],
      deployPort: [{ required: true, message: '请输入部署端口' }],
      deployUsername: [{ required: true, message: '请输入部署用户名' }],
      deployPassword: [{ required: true, message: '请输入部署密码' }],
    },

  });
  const { queryParams, deploySaveForm, rules } = toRefs(data);

  const deployList = ref<DeployResponse[]>([]);
  const total = ref(0);

  const deploy = ref<DeployResponse>({
    id: undefined,
    adminId: undefined,
    deployName: '',
    description: '',
    softwareId: undefined,
    serverIds: '',
    deployLogs: '',
    initStatus: undefined,
    status: undefined,
    createdBy: '',
    updatedBy: '',
    createdAt: '',
    updatedAt: ''
  });

  const fetchData = (params: DeployParam = {...queryParams.value}) => {
    setLoading(true);
    pageDeploy(params).then(res => {
      deployList.value = res.data.list;
      total.value = Number(res.data.total);
    }).finally(() => {
      setLoading(false);
    });

  };

  fetchData();
  const reset = () => {
    deploySaveForm.value = {
      id: undefined,
      adminId: undefined,
      deployName: '',
      description: '',
      softwareId: undefined,
      serverIds: '',
      deployLogs: '',
      initStatus: undefined,
      status: undefined,
    };
    proxy.$refs.formRef?.reserFields();
  };

  const visible = ref(false);
  const toAdd = () => {
    reset();
    title.value = '新增部署';
    visible.value = true;

    listSoftware({}).then(res => {
      selectData.value = res.data;
    });

    getServerOptions();
  };



  const toUpdate = (id: number) => {
    reset();
    getDeploy(id).then((res) => {
      deploySaveForm.value = res.data as unknown as DeploySaveCommand;
      title.value = '修改部署';
      visible.value = true;
    });

    listSoftware({}).then(res => {
      selectData.value = res.data;
    });
    getServerOptions();
  };


  const handleQuery = () => {
    console.log("handleQuery", queryParams);
    fetchData();
  };

  const resetQuery = () => {
    proxy.$refs.queryRef.resetFields();
    handleQuery();
  };

  const handlePageChange = (current: number) => {
    queryParams.value.page = current;
    fetchData();
  };

  const handlePageSizeChange = (pageSize: number) => {
    queryParams.value.size = pageSize;
    fetchData();
  };

  const handleSelectionChange = (rowKeys: Array<any>) => {
    checkedIds.value = rowKeys;
    single.value = rowKeys.length !== 1;
    multiple.value = !rowKeys.length;
  };


  const handleCancel = () => {
    visible.value = false;
    proxy.$refs.formRef?.resetFields();
    proxy.$refs.userRoleFormRef?.resetFields();
  };


  const selectedOptions = ref([]);
  const handleOk = () => {
    proxy.$refs.formRef.validate((valid: any) => {
      if (!valid) {
        if (deploySaveForm.value.id !== undefined) {
          deploySaveForm.value.serverIds = selectedOptions.value.map(options => options).join(',');
          updateDeploy(deploySaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            selectedOptions.value = [];
            proxy.$message.success(res.message);
          });
        } else {
          deploySaveForm.value.serverIds = selectedOptions.value.map(options => options).join(',');
          addDeploy(deploySaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            selectedOptions.value = [];
            proxy.$message.success(res.message);
          });
        }
      }
    });
  };

  const handleDelete = (ids: Array<number>) => {
    deleteDeploy(ids).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
  };

  const handleDeploy = (id: number) => {
    deployDeploy(id).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
  };

  const handleStartDeploy = (id: number) => {
    startDeploy(id).then((res:any) => {
      Notification.info({
        title:'启动日志',
        content: res.data,
        closable: true,
        duration: 60 * 1000
      })
    });
  };

  const handleSyncSoftware = () => {

    if(checkedIds.value.length === 0) {
      proxy.$message.info('没有选中数据');
    }else {
      proxy.$modal.warning({
        title: '警告',
        titleAlign: "start",
        content: '确定初始化吗？',
        hideCancel: false,
        onOk: () => {
          syncSoftware(checkedIds.value[0]).then((res:any) => {
            proxy.$message.success(res.message);
            fetchData();
          });
        }
      });
    }
  };
  

  const handleStatusDeploy = (id: number) => {
    statusDeploy(id).then((res:any) => {
      Notification.info({
        title:'状态日志',
        content: res.data,
        closable: true,
        duration: 60 * 1000
      })
    });
  };


  const detailLoading = ref(false);
  const detailVisible = ref(false);
  const toDetail = async (id: number) => {
    if (detailLoading.value) return;
    detailLoading.value = true;
    detailVisible.value = true;
    getDeploy(id)
        .then((res) => {
          deploy.value = res.data;
        })
        .finally(() => {
          detailLoading.value = false;
        });
  };

  const handleDetailCancel = () => {
    detailVisible.value = false;
  };

  const handleBatchDelete = () => {
    if(checkedIds.value.length === 0) {
      proxy.$message.info('没有选中数据');
    }else {
      proxy.$modal.warning({
        title: '警告',
        titleAlign: "start",
        content: '确定删除吗？',
        hideCancel: false,
        onOk: () => {
          handleDelete(checkedIds.value);
        }
      });
    }
  }

  const handleInit = () => {

    if(checkedIds.value.length === 0) {
      proxy.$message.info('没有选中数据');
    }else {
      proxy.$modal.warning({
        title: '警告',
        titleAlign: "start",
        content: '确定初始化吗？',
        hideCancel: false,
        onOk: () => {
          initDeploy(checkedIds.value[0]).then((res:any) => {
            proxy.$message.success(res.message);
            fetchData();
          });
        }
      });
    }
  };


  const serverIdsLoading = ref(false);
  const serverOptions = ref<DictResponseState[]>([]);

  const getServerOptions = () => {
    serverIdsLoading.value = true;
    listServer({})
        .then((res) => {
          serverOptions.value = res.data.map(({id, serverName}) => ({
            label: serverName,
            value: id
          }));
        })
        .finally(() => {
          serverIdsLoading.value = false;
        });
  };


  const exportLoading = ref(false);
  const handleExport = () => {
    if(exportLoading.value) return;

    exportLoading.value = true;
    proxy
        .download('/admin/ops/deploy/export', { ...queryParams.value }, '部署数据')
        .finally(() => {
          exportLoading.value = false;
        });

  }




  // 编辑器实例，必须用 shallowRef
  const editorRef = shallowRef()

  const editorConfig = { placeholder: '请输入内容...' }

  // 组件销毁时，也及时销毁编辑器
  onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
  })



</script>

<script lang="ts">
export default {
  name: 'SearchTable'
};
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.header {
  padding-bottom: 10px;
}
</style>
