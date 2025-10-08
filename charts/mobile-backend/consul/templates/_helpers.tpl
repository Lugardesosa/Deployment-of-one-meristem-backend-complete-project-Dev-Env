
{{/*
Return the fully qualified name of the chart
*/}}
{{- define "consul.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/*
Return chart name
*/}}
{{- define "consul.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

