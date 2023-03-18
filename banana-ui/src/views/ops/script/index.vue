<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', 'menu.ops.script.list']" />
    <a-card class="general-card" :title="$t('menu.ops.script.list')">

      <!-- 头部区域 -->
      <div class="header">
        <!-- 搜索栏 -->
        <div v-if="showQuery" class="header-query">
          <a-form ref="queryRef" :model="queryParams" layout="inline">
            <a-form-item field="name" hide-label>
              <a-input
                  v-model="queryParams.scriptName"
                  placeholder="输入脚本名或描述进行搜索"
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
                <a-button v-permission="['ops:script:add']" type="primary" @click="toAdd">
                  <template #icon><icon-plus/></template>新增
                </a-button>

                <a-button
                    v-permission="['ops:script:update']"
                    type="primary"
                    status="success"
                    :disabled="single"
                    :title="single ? '请选择一条要修改的数据' : ''"
                    @click="toUpdate(checkedIds[0])"
                >
                  <template #icon><icon-edit /></template>修改
                </a-button>
                <a-button
                    v-permission="['ops:script:delete']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要删除的数据' : ''"
                    @click="handleBatchDelete"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
                <a-button
                    v-permission="['ops:script:export']"
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
          :data="scriptList"
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
          <a-table-column title="脚本名" :width="80">
            <template #cell="{ record }">
              <a-link @click="toDetail(record.id)">{{
                  record.scriptName
                }}</a-link>
            </template>
          </a-table-column>

          <a-table-column title="脚本路径" data-index="scriptPath" :width="100" />
          <a-table-column title="脚本描述" data-index="description" :width="100" />

          <a-table-column title="创建时间" :width="100">
            <template #cell="{ record }">
              {{ record.createdAt }}
            </template>
          </a-table-column>

          <a-table-column title="操作" align="center" fixed="right" :width="100">

            <template #cell="{ record }">


              <a-button
                  v-permission="['ops:script:update']"
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
                    v-permission="['ops:script:delete']"
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
    <a-form ref="formRef" :model="scriptSaveForm" :rules="rules" :label-col-style="{ width: '94px' }"
          size="large" layout="inline" auto-label-width >

        <a-form-item label="脚本名" field="scriptName">
          <a-input
              v-model="scriptSaveForm.scriptName"
              placeholder="请输入脚本名"
              style="width: 500px"
          />
        </a-form-item>

        <a-form-item label="脚本路径" field="scriptPath">
          <a-input
              v-model="scriptSaveForm.scriptPath"
              placeholder="请输入脚本路径"
              style="width: 500px"
          />
        </a-form-item>


        <a-form-item label="脚本描述" field="description">
          <a-input
              v-model="scriptSaveForm.description"
              placeholder="请输入脚本描述"
              style="width: 500px"
          />
        </a-form-item>

        <a-form-item label="脚本内容" field="scriptContent">
          <a-textarea
              v-model="scriptSaveForm.scriptContent"
              placeholder="请输入脚本内容"
              style="width: 500px; height: 500px;"
          />
        </a-form-item>


      </a-form>
    </a-drawer>


    <a-drawer
        title="脚本详情"
        :visible="detailVisible"
        :width="700"
        :footer="false"
        unmount-on-close
        render-to-body
        @cancel="handleDetailCancel"
    >
      <a-descriptions :column="1" bordered size="large">

        <a-descriptions-item label="脚本名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.scriptTitle }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="脚本文件名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.scriptName }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="脚本文件路径">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.scriptPath }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="脚本描述">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.description }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="脚本内容">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.scriptContent }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.createdBy }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="更新者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.updatedBy }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.createdAt }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="更新时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ script.updatedAt }}</span>
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
    addScript,
    getScript,
    updateScript,
    deleteScript,
    ScriptSaveCommand,
    ScriptParam,
    ScriptResponse,
    pageScript
  } from "@/api/ops/script";

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
      scriptName: undefined,
      page: 1,
      size: 10,
      sort: ['createdAt,desc'],
    },
    scriptSaveForm: {} as ScriptSaveCommand,
    rules: {
      scriptName: [{ required: true, message: '请输入脚本名称' }],
      scriptPath: [{ required: true, message: '请输入脚本路径' }]
    },

  });
  const { queryParams, scriptSaveForm, rules } = toRefs(data);

  const scriptList = ref<ScriptResponse[]>([]);
  const total = ref(0);

  const script = ref<ScriptResponse>({
    id: undefined,
    scriptTitle: '',
    scriptName: '',
    scriptPath: '',
    scriptContent: '',
    description: '',
    status: undefined,
    createdBy: '',
    updatedBy: '',
    createdAt: '',
    updatedAt: ''
  });

  const fetchData = (params: ScriptParam = {...queryParams.value}) => {
    setLoading(true);
    pageScript(params).then(res => {
      scriptList.value = res.data.list;
      total.value = Number(res.data.total);
    }).finally(() => {
      setLoading(false);
    });

  };

  fetchData();
  const reset = () => {
    scriptSaveForm.value = {
      id: undefined,
      scriptTitle: '',
      scriptName: '',
      scriptPath: '',
      scriptContent: '',
      description: '',
      status: undefined,
    };
    proxy.$refs.formRef?.reserFields();
  };

  const visible = ref(false);
  const toAdd = () => {
    reset();
    title.value = '新增脚本';
    visible.value = true;
  };


  const toUpdate = (id: number) => {
    reset();
    getScript(id).then((res) => {
      scriptSaveForm.value = res.data as unknown as ScriptSaveCommand;
      title.value = '修改脚本';
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
        if (scriptSaveForm.value.id !== undefined) {
          updateScript(scriptSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        } else {
          addScript(scriptSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        }
      }
    });
  };

  const handleDelete = (ids: Array<number>) => {
    deleteScript(ids).then((res:any) => {
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
    getScript(id)
        .then((res) => {
          script.value = res.data;
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
        .download('/admin/ops/script/export', { ...queryParams.value }, '脚本数据')
        .finally(() => {
          exportLoading.value = false;
        });

  }




  // 编辑器实例，必须用 shallowRef
  const editorRef = shallowRef()

  const editorConfig = { placeholder: '请输入内容...' }

  // 脚本销毁时，也及时销毁编辑器
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
