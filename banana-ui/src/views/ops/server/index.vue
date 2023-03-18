<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', 'menu.ops.server.list']" />
    <a-card class="general-card" :title="$t('menu.ops.server.list')">

      <!-- 头部区域 -->
      <div class="header">
        <!-- 搜索栏 -->
        <div v-if="showQuery" class="header-query">
          <a-form ref="queryRef" :model="queryParams" layout="inline">
            <a-form-item field="name" hide-label>
              <a-input
                  v-model="queryParams.serverName"
                  placeholder="输入服务实例名或描述进行搜索"
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
                <a-button v-permission="['ops:server:add']" type="primary" @click="toAdd">
                  <template #icon><icon-plus/></template>新增
                </a-button>

                <a-button
                    v-permission="['ops:server:update']"
                    type="primary"
                    status="success"
                    :disabled="single"
                    :title="single ? '请选择一条要修改的数据' : ''"
                    @click="toUpdate(checkedIds[0])"
                >
                  <template #icon><icon-edit /></template>修改
                </a-button>
                <a-button
                    v-permission="['ops:server:delete']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要删除的数据' : ''"
                    @click="handleBatchDelete"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
                <a-button
                    v-permission="['ops:server:export']"
                    :loading="exportLoading"
                    type="primary"
                    status="warning"
                    @click="handleExport"
                >
                  <template #icon><icon-download /></template>导出
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
          :data="serverList"
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
          <a-table-column title="服务实例名" :width="200">
            <template #cell="{ record }">
              <a-link @click="toDetail(record.id)">{{
                  record.serverName
                }}</a-link>
            </template>
          </a-table-column>

          <a-table-column title="外网IP" data-index="serverOuterIp" :width="140" >
            <template #cell="{ record }">
              <a-typography-paragraph copyable>
                {{ record.serverOuterIp }}
              </a-typography-paragraph>
            </template>
          </a-table-column>
          
          <a-table-column title="内网IP" data-index="serverInnerIp" :width="100" />
          <a-table-column title="端口" data-index="serverPort" :width="50" />
          <a-table-column title="用户名" data-index="serverUsername" :width="100" />
          <a-table-column title="密码" data-index="serverPassword" :width="100" />


          <a-table-column title="平台" :width="70">
            <template #cell="{ record }">
              <span v-if="record.platformType === 1">自建</span>
              <span v-else-if="record.platformType === 2">阿里云</span>
              <span v-else-if="record.platformType === 3">腾讯云</span>
              <span v-else>未知</span>
            </template>
          </a-table-column>

          <a-table-column title="创建时间" :width="130">
            <template #cell="{ record }">
              {{ record.createdAt }}
            </template>
          </a-table-column>

          <a-table-column title="操作" align="center" fixed="right" :width="250">

            <template #cell="{ record }">


              <a-button
                  v-permission="['ops:server:update']"
                  type="text"
                  size="small"
                  title="修改"
                  @click="toUpdate(record.id)"
              >
                <template #icon><icon-edit /></template>修改
              </a-button>

              <a-popconfirm
                  content="确定删除吗？"
                  type="warning"
                  @ok="handleDelete([record.id])"
              >
                <a-button
                    v-permission="['ops:server:delete']"
                    type="text"
                    size="small"
                    title="删除"
                    :disabled="record.disabled"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
              </a-popconfirm>
            </template>

          </a-table-column>
        </template>


      </a-table>
    </a-card>




    <!-- 表单区域 -->
    <a-drawer :title="title" :visible="visible" :width="690" :mask-closable="false" unmount-on-close
        render-to-body @ok="handleOk" @cancel="handleCancel"
    >
    <a-form ref="formRef" :model="serverSaveForm" :rules="rules" :label-col-style="{ width: '94px' }"
          size="large" layout="inline" auto-label-width >

        <a-form-item label="服务实例名" field="serverName">
          <a-input
              v-model="serverSaveForm.serverName"
              placeholder="请输入服务实例名"
              style="width: 162px"
          />
        </a-form-item>

        <a-form-item label="外网IP" field="serverOuterIp">
          <a-input
              v-model="serverSaveForm.serverOuterIp"
              placeholder="请输入外网IP"
              style="width: 162px"
          />
        </a-form-item>


        <a-form-item label="内网IP" field="serverInnerIp">
          <a-input
              v-model="serverSaveForm.serverInnerIp"
              placeholder="请输入内网IP"
              style="width: 162px"
          />
        </a-form-item>

        <a-form-item label="端口" field="serverPort">
          <a-input
              v-model="serverSaveForm.serverPort"
              placeholder="请输入端口"
              style="width: 162px"
          />
        </a-form-item>

        <a-form-item label="用户名" field="serverUsername">
          <a-input
              v-model="serverSaveForm.serverUsername"
              placeholder="请输入用户名"
              style="width: 162px"
          />
        </a-form-item>

        <a-form-item label="密码" field="serverPassword">
          <a-input
              v-model="serverSaveForm.serverPassword"
              placeholder="请输入密码"
              style="width: 162px"
          />
        </a-form-item>

        <a-form-item label="平台" field="platformType">
          <a-radio-group v-model="serverSaveForm.platformType">
            <a-radio :value="1">自建</a-radio>
            <a-radio :value="2">阿里云</a-radio>
            <a-radio :value="3">腾讯云</a-radio>
          </a-radio-group>
        </a-form-item>

      </a-form>
    </a-drawer>


    <a-drawer
        title="服务实例详情"
        :visible="detailVisible"
        :width="700"
        :footer="false"
        unmount-on-close
        render-to-body
        @cancel="handleDetailCancel"
    >
      <a-descriptions :column="2" bordered size="large">

        <a-descriptions-item label="服务实例名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.serverName }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="外网IP">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.serverOuterIp }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="内网IP">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.serverInnerIp }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="端口">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.serverPort }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="用户名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.serverUsername }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="密码">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.serverPassword }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="平台">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>
              <a-tag v-if="server.platformType === 1">自建</a-tag>
              <a-tag v-if="server.platformType === 2">阿里云</a-tag>
              <a-tag v-if="server.platformType === 3">腾讯云</a-tag>
            </span>
        </a-descriptions-item>

        <a-descriptions-item label="创建时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.createdAt }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.createdBy }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="更新者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.updatedBy }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="更新时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ server.updatedAt }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="更新时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <a-button type="primary" @click="handleConnectTest(server)"> <template #icon><icon-cloud /></template>连接测试 </a-button>

        </a-descriptions-item>


      </a-descriptions>
    </a-drawer>

  </div>



</template>

<script lang="ts" setup>
  import {getCurrentInstance, reactive, ref, toRefs} from 'vue';
  import {useI18n} from 'vue-i18n';
  import useLoading from '@/hooks/loading';
  import {PolicyRecord} from '@/api/list';
  import {
    addServer,
    getServer,
    updateServer,
    deleteServer,
    ServerSaveCommand,
    ServerParam,
    ServerResponse,
    pageServer,
    generateServer,
    connectTest
  } from "@/api/ops/server";
  import {DictResponseState} from "@/store/modules/dict/types";
  import '@wangeditor/editor/dist/css/style.css' // 引入 css

  import { onBeforeUnmount, shallowRef, onMounted } from 'vue'
  import { Boot } from '@wangeditor/editor'
  import markdownModule from '@wangeditor/plugin-md'
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue'


  const showQuery = ref(true);
  Boot.registerModule(markdownModule)




  type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  const { proxy } = getCurrentInstance() as any;
  const { loading, setLoading } = useLoading(true);
  const { t } = useI18n();
  const renderData = ref<PolicyRecord[]>([]);

  const size = ref<SizeProps>('medium');
  const checkedIds = ref<Array<number>>([]);
  const title = ref('');
  const single = ref(true);
  const multiple = ref(true);

  const data = reactive({
    queryParams: {
      serverName: undefined,
      page: 1,
      size: 10,
      sort: ['createdAt,desc'],
    },
    serverSaveForm: {} as ServerSaveCommand,
    rules: {
      serverName: [{ required: true, message: '请输入服务实例名' }],
      serverOuterIp: [{ required: true, message: '请输入服务实例外网IP' }],
      serverPort: [{ required: true, message: '请输入服务实例端口' }],
      serverUsername: [{ required: true, message: '请输入服务实例用户名' }],
      serverPassword: [{ required: true, message: '请输入服务实例密码' }],
    },

  });
  const { queryParams, serverSaveForm, rules } = toRefs(data);

  const serverList = ref<ServerResponse[]>([]);
  const total = ref(0);

  const server = ref<ServerResponse>({
    id: undefined,
    adminId: undefined,
    serverName: '',
    serverOuterIp: '',
    serverInnerIp: '',
    serverPort: '',
    serverUsername: '',
    serverPassword: '',
    platformType: undefined,
    createdBy: '',
    updatedBy: '',
    createdAt: '',
    updatedAt: ''
  });

  const fetchData = (params: ServerParam = {...queryParams.value}) => {
    setLoading(true);
    pageServer(params).then(res => {
      serverList.value = res.data.list;
      total.value = Number(res.data.total);
    }).finally(() => {
      setLoading(false);
    });

  };

  fetchData();
  const reset = () => {
    serverSaveForm.value = {
      id: undefined,
      adminId: undefined,
      serverName: '',
      serverOuterIp: '',
      serverInnerIp: '',
      serverPort: '',
      serverUsername: '',
      serverPassword: '',
      platformType: undefined
    };
    proxy.$refs.formRef?.reserFields();
  };

  const visible = ref(false);
  const toAdd = () => {
    reset();
    title.value = '新增服务实例';
    visible.value = true;
  };

  const roleLoading = ref(false);
  const roleOptions = ref<DictResponseState[]>([]);


  const toUpdate = (id: number) => {
    reset();
    getServer(id).then((res) => {
      serverSaveForm.value = res.data as unknown as ServerSaveCommand;
      title.value = '修改服务实例';
      visible.value = true;
    });
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

  const handleOk = () => {
    proxy.$refs.formRef.validate((valid: any) => {
      if (!valid) {
        if (serverSaveForm.value.id !== undefined) {
          updateServer(serverSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        } else {
          addServer(serverSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        }
      }
    });
  };

  const handleDelete = (ids: Array<number>) => {
    deleteServer(ids).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
  };

  const handleGenerate = (id: number) => {
    generateServer(id).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
  };

  const handleConnectTest = (server: ServerResponse) => {
    connectTest(server).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
  };



  
  

  const detailLoading = ref(false);
  const detailVisible = ref(false);
  const toDetail = async (id: number) => {
    if (detailLoading.value) return;
    detailLoading.value = true;
    detailVisible.value = true;
    getServer(id)
        .then((res) => {
          server.value = res.data;
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

  const exportLoading = ref(false);
  const handleExport = () => {
    if(exportLoading.value) return;

    exportLoading.value = true;
    proxy
        .download('/admin/ops/server/export', { ...queryParams.value }, '服务实例数据')
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
