<template>
    <div>
        <DateScopePicker></DateScopePicker>
        <br />
        <br />
        <Vscode></Vscode>
        <Idea></Idea>

        <Chrome></Chrome>

    </div>
</template>
  
<script>

import DateScopePicker from './code/DateScopePicker.vue';
import Vscode from './code/Vscode.vue';
import Idea from './code/Idea.vue';
import Chrome from './code/Chrome.vue';

export default {
    name: 'App',
    components: {
        DateScopePicker,
        Vscode,
        Idea,
        Chrome
    }
}
</script>