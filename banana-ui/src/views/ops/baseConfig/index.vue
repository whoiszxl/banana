<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', 'menu.ops.baseConfig.list']" />
    <a-card class="general-card" :title="$t('menu.ops.baseConfig.list')">

      <!-- 头部区域 -->
      <div class="header">
        <!-- 搜索栏 -->
        <div v-if="showQuery" class="header-query">
          <a-form ref="queryRef" :model="queryParams" layout="inline">
            <a-form-item field="name" hide-label>
              <a-input
                  v-model="queryParams.baseConfigName"
                  placeholder="输入基础配置名或描述进行搜索"
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
                <a-button v-permission="['ops:baseConfig:add']" type="primary" @click="toAdd">
                  <template #icon><icon-plus/></template>新增
                </a-button>

                <a-button
                    v-permission="['ops:baseConfig:update']"
                    type="primary"
                    status="success"
                    :disabled="single"
                    :title="single ? '请选择一条要修改的数据' : ''"
                    @click="toUpdate(checkedIds[0])"
                >
                  <template #icon><icon-edit /></template>修改
                </a-button>
                <a-button
                    v-permission="['ops:baseConfig:delete']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要删除的数据' : ''"
                    @click="handleBatchDelete"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
                <a-button
                    v-permission="['ops:baseConfig:export']"
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
          :data="baseConfigList"
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
          <a-table-column title="ID" data-index="id" :width="30" />

          <a-table-column title="配置KEY" data-index="configKey" :width="100" />
          <a-table-column title="配置VALUE" data-index="configValue" :width="100" />

          <a-table-column title="创建时间" :width="100">
            <template #cell="{ record }">
              {{ record.createdAt }}
            </template>
          </a-table-column>

          <a-table-column title="操作" align="center" fixed="right" :width="100">

            <template #cell="{ record }">


              <a-button
                  v-permission="['ops:baseConfig:update']"
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
                    v-permission="['ops:baseConfig:delete']"
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
    <a-form ref="formRef" :model="baseConfigSaveForm" :rules="rules" :label-col-style="{ width: '94px' }"
          size="large" layout="inline" auto-label-width >

        <a-form-item label="配置KEY" field="configKey">
          <a-input
              v-model="baseConfigSaveForm.configKey"
              placeholder="请输入配置KEY"
              style="width: 500px"
          />
        </a-form-item>

        <a-form-item label="配置VALUE" field="configValue">
          <a-input
              v-model="baseConfigSaveForm.configValue"
              placeholder="请输入配置VALUE"
              style="width: 500px"
          />
        </a-form-item>

      </a-form>
    </a-drawer>


  </div>



</template>

<script lang="ts" setup>
  import {getCurrentInstance, reactive, ref, toRefs} from 'vue';
  import {useI18n} from 'vue-i18n';
  import useLoading from '@/hooks/loading';
  import {PolicyRecord} from '@/api/list';
  import {
    addBaseConfig,
    getBaseConfig,
    updateBaseConfig,
    deleteBaseConfig,
    BaseConfigSaveCommand,
    BaseConfigParam,
    BaseConfigResponse,
    pageBaseConfig
  } from "@/api/ops/baseConfig";

  import { onBeforeUnmount, shallowRef } from 'vue'
  import { Boot } from '@wangeditor/editor'
  import markdownModule from '@wangeditor/plugin-md'


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
      baseConfigName: undefined,
      page: 1,
      size: 10,
      sort: ['createdAt,desc'],
    },
    baseConfigSaveForm: {} as BaseConfigSaveCommand,
    rules: {
      configKey: [{ required: true, message: '请输入配置KEY' }],
      configValue: [{ required: true, message: '请输入配置VALUE' }]
    },

  });
  const { queryParams, baseConfigSaveForm, rules } = toRefs(data);

  const baseConfigList = ref<BaseConfigResponse[]>([]);
  const total = ref(0);

  const baseConfig = ref<BaseConfigResponse>({
    id: undefined,
    configKey: '',
    configValue: '',
    status: undefined,
    createdBy: '',
    updatedBy: '',
    createdAt: '',
    updatedAt: ''
  });

  const fetchData = (params: BaseConfigParam = {...queryParams.value}) => {
    setLoading(true);
    pageBaseConfig(params).then(res => {
      baseConfigList.value = res.data.list;
      total.value = Number(res.data.total);
    }).finally(() => {
      setLoading(false);
    });

  };

  fetchData();
  const reset = () => {
    baseConfigSaveForm.value = {
      id: undefined,
      configKey: '',
      configValue: '',
      status: undefined,
    };
    proxy.$refs.formRef?.reserFields();
  };

  const visible = ref(false);
  const toAdd = () => {
    reset();
    title.value = '新增基础配置';
    visible.value = true;
  };


  const toUpdate = (id: number) => {
    reset();
    getBaseConfig(id).then((res) => {
      baseConfigSaveForm.value = res.data as unknown as BaseConfigSaveCommand;
      title.value = '修改基础配置';
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
        if (baseConfigSaveForm.value.id !== undefined) {
          updateBaseConfig(baseConfigSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        } else {
          addBaseConfig(baseConfigSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        }
      }
    });
  };

  const handleDelete = (ids: Array<number>) => {
    deleteBaseConfig(ids).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
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
        .download('/admin/ops/baseConfig/export', { ...queryParams.value }, '基础配置数据')
        .finally(() => {
          exportLoading.value = false;
        });

  }




  // 编辑器实例，必须用 shallowRef
  const editorRef = shallowRef()

  const editorConfig = { placeholder: '请输入内容...' }

  // 基础配置销毁时，也及时销毁编辑器
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
